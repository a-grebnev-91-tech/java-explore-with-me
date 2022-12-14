package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.validation.EventDateInFuture;
import ru.practicum.validation.ExistingCategory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.util.Constants.DEFAULT_DATE_TIME_FORMAT;

@Getter
@Setter
public class NewEventDto {
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
    @NotNull
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    @Size(min = 20, max = 7000)
    private String description;
    @EventDateInFuture
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DEFAULT_DATE_TIME_FORMAT)
    private LocalDateTime eventDate;
    @ExistingCategory
    private Long category;
    private Boolean paid = false;
    private Boolean requestModeration = true;
    @PositiveOrZero
    private Integer participantLimit = 0;
    @NotNull
    private Location location;
}
