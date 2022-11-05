package ru.practicum.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class EventNotification {
    @NotNull
    private long eventId;
    @NotNull
    private String title;
    @NotNull
    private String annotation;
    @NotNull
    private long initiatorId;
    @NotNull
    private String initiatorName;
    @NotNull
    private LocalDateTime eventDate;
    private List<Long> participantsIds = new ArrayList<>();
    @NotNull
    private EventAction action;
}
