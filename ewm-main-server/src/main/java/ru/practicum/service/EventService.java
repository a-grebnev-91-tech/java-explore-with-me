package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.Event;
import ru.practicum.exception.ForbiddenOperationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.EventState;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.util.OffsetPageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final RequestRepository requestRepo;
    private final EventMapper mapper;

    public EventFullDto addEvent(NewEventDto event, long userId) {
        checkUserExistingOrThrow(userId);
        Event entity = mapper.dtoToEntity(event, userId);
        entity = eventRepo.save(entity);
        return mapper.modelToFullDto(entity);
    }

    public EventFullDto cancelById(long userId, long eventId) {
        checkUserExistingOrThrow(userId);
        Event event = getEventOrThrow(eventId);
        if (event.getState() != EventState.PENDING)
            throw new ForbiddenOperationException(
                    "Only an event in PENDING state could be canceled",
                    String.format("Event with id %d in %s state", eventId, event.getState().toString())
            );
        event.setState(EventState.CANCELED);
        return mapper.modelToFullDto(event);
    }

    public List<EventShortDto> findByInitiator(long userId, int from, int size) {
        checkUserExistingOrThrow(userId);
        OffsetPageable pageable = OffsetPageable.of(from, size);

        return null;
    }

    public EventFullDto findById(long userId, long eventId) {
        return null;
    }

    public EventFullDto updateByInitiator(long userId, UpdateEventRequest dto) {
        return null;
    }

    public List<ParticipationRequestDto> findParticipationRequests(long userId, long eventId) {
        return null;
    }

    private void checkUserExistingOrThrow(long userId) {
        if (!userRepo.existsById(userId))
            throw new NotFoundException("User not found", String.format("User with id %d isn't exist", userId));
    }

    private Event getEventOrThrow(long eventId) {
        return eventRepo.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event not found", String.format("Event with id %d isn't exist", eventId))
        );
    }
}
