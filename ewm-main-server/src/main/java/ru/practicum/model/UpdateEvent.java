package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.entity.Category;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateEvent {
    private String annotation;
    private Category category;
    private String description;
    private LocalDateTime eventDate;
    private Double lat;
    private Double lon;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
