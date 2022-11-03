package ru.practicum.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestNotification {
    private final Long id;
    private final EventNotification event;
    private final Long requesterId;
    private final RequestAction action;
}
