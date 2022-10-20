package ru.practicum.service;

import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

@Service
public class EventService {
    public List<EventShortDto> findByInitiator(long userId, int from, int size) {
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

    public EventFullDto findById(long userId, long eventId) {
        return null;
    }

    public ParticipationRequestDto confirmRequest(long userId, long eventId, long reqId) {
        return null;
    }

    public EventFullDto cancelById(long userId, long eventId) {
        return null;
    }
}
