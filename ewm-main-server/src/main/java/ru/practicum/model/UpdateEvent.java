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

    public boolean hasAnnotation() {
        return annotation != null && !annotation.isBlank();
    }

    public boolean hasCategory() {
        return category != null;
    }

    public boolean hasDescription() {
        return description != null && !description.isBlank();
    }

    public boolean hasEventDate() {
        return eventDate != null;
    }

    public boolean hasLat() {
        return lat != null;
    }

    public boolean hasLon() {
        return lon != null;
    }

    public boolean hasPaid() {
        return paid != null;
    }

    public boolean hasParticipantLimit() {
        return participantLimit != null;
    }

    public boolean hasRequestModeration() {
        return requestModeration != null;
    }

    public boolean hasTitle() {
        return title != null && !title.isBlank();
    }
}
