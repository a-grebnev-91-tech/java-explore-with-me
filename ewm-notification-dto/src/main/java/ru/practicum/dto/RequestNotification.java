package ru.practicum.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RequestNotification {
    private Long requestId;
    private Long eventId;
    private Long eventInitiatorId;
    private String eventTitle;
    private String eventAnnotation;
    private Long requesterId;
    private Long requesterName;
    private RequestAction action;
}
