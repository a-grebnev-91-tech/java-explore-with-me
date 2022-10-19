package ru.practicum.service;

import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.UpdateEventRequest;

import java.util.List;

public class EventService {
    public List<EventShortDto> findByInitiator(long userId, int from, int size) {
        return null;
    }

    public EventFullDto updateByInitiator(long userId, UpdateEventRequest dto) {
        return null;
    }
}
