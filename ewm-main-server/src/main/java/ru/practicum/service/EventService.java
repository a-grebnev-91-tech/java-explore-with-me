package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.event.*;
import ru.practicum.dto.stats.EndpointHit;
import ru.practicum.dto.stats.ViewStats;
import ru.practicum.entity.Event;
import ru.practicum.exception.ForbiddenOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.EventOrderBy;
import ru.practicum.model.EventState;
import ru.practicum.model.UpdateEvent;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.AdminEventParamObj;
import ru.practicum.util.OffsetPageable;
import ru.practicum.util.PublicEventParamObj;
import ru.practicum.util.Patcher;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final StatsClient statsClient;
    private final NotificationService notificationService;
    private final EventMapper mapper;
    private final Patcher patcher;

    public EventFullDto addEvent(NewEventDto event, long userId) {
        checkUserExistingOrThrow(userId);
        Event entity = mapper.dtoToEntity(event, userId);
        entity = eventRepo.save(entity);
        log.info("User with ID {} create event with ID {}", userId, entity.getId());
        return mapper.entityToFullDto(entity);
    }

    public EventFullDto cancelById(long userId, long eventId) {
        Event event = getEventOrThrow(eventId);
        checkInitiatorOrThrow(event, userId);
        if (event.getState() != EventState.PENDING)
            throw new ForbiddenOperationException(
                    "Only an event in PENDING state could be canceled",
                    String.format("Event with id %d in %s state", eventId, event.getState().toString())
            );
        event.setState(EventState.CANCELED);
        updateViews(event);
        log.info("Initiator with ID {} canceled event with ID {}", userId, eventId);
        return mapper.entityToFullDto(event);
    }

    public List<EventShortDto> findAll(PublicEventParamObj paramObj, String ip, String uri) {
        List<Event> events = eventRepo.findAllByPublicParams(paramObj);
        log.info("Found {} events", events.size());
        updateViews(events.toArray(new Event[0]));
        if (paramObj.getOrderBy() == EventOrderBy.VIEWS) {
            events = sortAndTrimByViews(events, paramObj);
        }
        writeStatistics(ip, uri);
        return mapper.batchModelToShortDto(events);
    }

    public List<EventFullDto> findAll(AdminEventParamObj paramObj) {
        List<Event> events = eventRepo.findAllByAdminParams(paramObj);
        updateViews(events.toArray(new Event[0]));
        return mapper.batchModelToFullDto(events);
    }

    public EventFullDto findById(long id, String ip, String url) {
        Event event = eventRepo.findByIdAndState(id, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Event not found", String.format("Event with id %d isn't exist", id))
        );
        log.info("Found event with ID {}", event.getId());
        updateViews(event);
        writeStatistics(ip, url);
        return mapper.entityToFullDto(event);
    }

    public List<EventShortDto> findByInitiator(long userId, int from, int size) {
        checkUserExistingOrThrow(userId);
        OffsetPageable pageable = OffsetPageable.of(from, size);
        List<Event> events = eventRepo.findByInitiatorId(userId, pageable);
        updateViews(events.toArray(new Event[0]));
        log.info("Initiator with ID {} received all his events", userId);
        return mapper.batchModelToShortDto(events);
    }

    public EventFullDto findByIdForInitiator(long initiatorId, long eventId) {
        Event event = getEventOrThrow(eventId);
        checkInitiatorOrThrow(event, initiatorId);
        updateViews(event);
        log.info("Initiator with ID {} received event with ID {}", initiatorId, eventId);
        return mapper.entityToFullDto(event);
    }

    public EventFullDto publishEvent(long id) {
        Event event = getEventOrThrow(id);
        couldBePublishedOrThrow(event);
        publish(event);
        updateViews(event);
        return mapper.entityToFullDto(event);
    }

    public EventFullDto rejectEvent(long id) {
        Event event = getEventOrThrow(id);
        couldBeRejectOrThrow(event);
        reject(event);
        notificationService.eventCanceled(event);
        updateViews(event);
        return mapper.entityToFullDto(event);
    }

    public EventFullDto updateByAdmin(long id, AdminUpdateEventRequest event) {
        Event originalEvent = getEventOrThrow(id);
        UpdateEvent patch = mapper.updateDtoToModel(event);
        patcher.patchEvent(originalEvent, patch);
        updateViews(originalEvent);
        log.info("Admin update event with ID {}", id);
        return mapper.entityToFullDto(originalEvent);
    }

    public EventFullDto updateByInitiator(long userId, UpdateEventRequest dto) {
        Event originalEvent = getEventOrThrow(dto.getEventId());
        checkInitiatorOrThrow(originalEvent, userId);
        switch (originalEvent.getState()) {
            case CANCELED:
                originalEvent.setState(EventState.PENDING);
                break;
            case PUBLISHED:
                throw new ForbiddenOperationException(
                        "It's not allowed to update an event in state PUBLISHED",
                        String.format("Event with id %d in %s state",
                                originalEvent.getId(), originalEvent.getState().toString()
                        )
                );
        }
        patcher.patchEvent(originalEvent, mapper.updateDtoToModel(dto));
        updateViews(originalEvent);
        log.info("Initiator with ID {} update event with ID {}", userId, dto.getEventId());
        return mapper.entityToFullDto(originalEvent);
    }

    private void checkUserExistingOrThrow(long userId) {
        if (!userRepo.existsById(userId))
            throw new NotFoundException("User not found", String.format("User with id %d isn't exist", userId));
    }

    private void checkInitiatorOrThrow(Event event, long userId) {
        if (event.getInitiator().getId() != userId) {
            checkUserExistingOrThrow(userId);
            throw new ForbiddenOperationException(
                    "Only the initiator has access to this operation",
                    String.format("User with id %d isn't initiator of event with id %d", userId, event.getId())
            );
        }
    }

    private void couldBePublishedOrThrow(Event event) {
        if (event.getState() != EventState.PENDING)
            throw new ForbiddenOperationException(
                    "Only PENDING events can be published",
                    String.format("Event with ID %d in %s state", event.getId(), event.getState())
            );
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1)))
            throw new ForbiddenOperationException(
                    "Only events in future could be published",
                    String.format("Before start of the event with ID %d less than hour", event.getId())
            );
    }

    private void couldBeRejectOrThrow(Event event) {
        if (event.getState() == EventState.PUBLISHED)
            throw new ForbiddenOperationException(
                    "Published events couldn't be rejected",
                    String.format("Event with ID %d have state %s", event.getId(), event.getState())
            );
    }

    private Event getEventOrThrow(long eventId) {
        return eventRepo.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", String.format("Event with id %d isn't exist", eventId))
        );
    }

    private void publish(Event event) {
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
    }

    private List<Event> sortAndTrimByViews(List<Event> events, PublicEventParamObj paramObj) {
        events.sort((o1, o2) -> Long.compare(o1.getViews(), o2.getViews()));
        return events.subList(paramObj.getOffset(), paramObj.getOffset() + paramObj.getSize());
    }

    private void reject(Event event) {
        event.setState(EventState.CANCELED);
    }

    private void updateViews(Event... events) {
        Map<Long, Event> eventMap = Arrays.stream(events).collect(Collectors.toMap(Event::getId, Function.identity()));
        List<String> uris = eventMap.keySet().stream().map(id -> "/events/" + id).collect(Collectors.toList());
        List<ViewStats> stats = statsClient.stats(uris);
        for (ViewStats stat : stats) {
            String[] uri = stat.getUri().split("/");
            long id = Long.parseLong(uri[uri.length - 1]);
            eventMap.get(id).setViews(stat.getHits());
        }
    }

    private void writeStatistics(String ip, String uri) {
        EndpointHit dto = new EndpointHit();
        dto.setApp("ewm-main-server");
        dto.setUri(uri);
        dto.setIp(ip);
        dto.setTimestamp(LocalDateTime.now());
        statsClient.hit(dto);
    }
}
