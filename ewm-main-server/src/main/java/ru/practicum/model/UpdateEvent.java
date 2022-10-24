package ru.practicum.model;

import ru.practicum.entity.Category;

import java.time.LocalDateTime;

public class UpdateEvent {
    private Long eventId;
    private String annotation;
    private Category category;
    private String description;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Integer participantLimit;
    private String title;
}
