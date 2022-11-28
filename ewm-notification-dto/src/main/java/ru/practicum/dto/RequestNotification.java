package ru.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class RequestNotification {
    @NotNull
    private Long requestId;
    @NotNull
    private Long eventId;
    @NotNull
    private Long eventInitiatorId;
    @NotNull
    private String eventTitle;
    @NotNull
    private String eventAnnotation;
    @NotNull
    private Long requesterId;
    @NotNull
    private String requesterName;
    @NotNull
    private RequestAction action;
}
