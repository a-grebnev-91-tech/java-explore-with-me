package ru.practicum.dto.event;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.validation.ExistingCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
public class NewEventDto {
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;
    @ExistingCategory
    private long category;
    @NotNull
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    private Instant eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private int participantLimit;
    private Boolean requestModeration;
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}
