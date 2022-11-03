package ru.practicum.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class EventNotification {
    @NotNull
    private final long eventId;
    @NotNull
    private final String title;
    @NotNull
    private final String annotation;
    @NotNull
    private final long initiatorId;
    @NotNull
    private final String initiatorName;
    @NotNull
    private final LocalDateTime eventDate;
    @NotNull
    private final EventAction action;
}
