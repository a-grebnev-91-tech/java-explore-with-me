package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.entity.Event;
import ru.practicum.exception.ForbiddenOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.PatchException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.EventState;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.OffsetPageable;
import ru.practicum.util.ParamObject;
import ru.practicum.util.Patcher;

import java.util.List;

//TODO check pre-moderation of requests
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final EventMapper mapper;

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
        log.info("Initiator with ID {} canceled event with ID {}", userId, eventId);
        return mapper.entityToFullDto(event);
    }

    //TODO add statistics
    public List<EventShortDto> findAll(ParamObject paramObj) {
//это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
//текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
//если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
//информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
//информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
//        throw new RuntimeException("Not impl");

        return mapper.batchModelToShortDto(eventRepo.findAllByQueryDsl(paramObj));
    }

    //TODO add statistics
    public EventFullDto findById(long id) {
//        событие должно быть опубликовано
//        информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
//        информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        throw new RuntimeException("not impl");
    }

    public List<EventShortDto> findByInitiator(long userId, int from, int size) {
        checkUserExistingOrThrow(userId);
        OffsetPageable pageable = OffsetPageable.of(from, size);
        log.info("Initiator with ID {} received all his events", userId);
        return mapper.batchModelToShortDto(eventRepo.findByInitiatorId(userId, pageable));
    }

    public EventFullDto findByIdForInitiator(long initiatorId, long eventId) {
        Event event = getEventOrThrow(eventId);
        checkInitiatorOrThrow(event, initiatorId);
        log.info("Initiator with ID {} received event with ID {}", initiatorId, eventId);
        return mapper.entityToFullDto(event);
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
        if (Patcher.patch(originalEvent, mapper.updateDtoToModel(dto))) {
            log.info("Initiator with ID {} update event with ID {}", userId, dto.getEventId());
            return mapper.entityToFullDto(originalEvent);
        } else {
            throw new PatchException(
                    "Error occurred",
                    String.format("Patch %s couldn't be applied on %s", dto, originalEvent)
            );
        }
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

    private Event getEventOrThrow(long eventId) {
        return eventRepo.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", String.format("Event with id %d isn't exist", eventId))
        );
    }
}
