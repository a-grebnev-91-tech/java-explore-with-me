package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.event.EventMapper;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final EventMapper mapper;

    public EventFullDto addEvent(NewEventDto event, long userId) {
        checkExistingOrThrow(userId);
        Event entity = mapper.dtoToEntity(event, userId);
        return null;
    }

    public EventFullDto cancelById(long userId, long eventId) {
        return null;
    }

    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        return null;
    }

    public List<EventShortDto> findByInitiator(long userId, int from, int size) {
        return null;
    }

    public EventFullDto findById(long userId, long eventId) {
        return null;
    }

    public EventFullDto updateByInitiator(long userId, UpdateEventRequest dto) {
        return null;
    }

    public ParticipationRequestDto rejectRequest(long userId, long eventId, long reqId) {
        return null;
    }

    public List<ParticipationRequestDto> findParticipationRequests(long userId, long eventId) {
        return null;
    }

    private void checkExistingOrThrow(long userId) {
        if (!userRepo.existsById(userId))
            throw new NotFoundException("User not found", String.format("User with id %d isn't exist", userId));
    }
}
