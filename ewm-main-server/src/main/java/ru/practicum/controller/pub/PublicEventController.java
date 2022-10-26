package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.model.EventOrderBy;
import ru.practicum.service.EventService;
import ru.practicum.util.ParamObject;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class PublicEventController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> findAll(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT_STRING)
            LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT_STRING)
            LocalDateTime rangeEnd,
            @RequestParam(value = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "orderBy", required = false) EventOrderBy orderBy,
            @RequestParam(value = "from", defaultValue = DEFAULT_FROM_VALUE) int from,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE_VALUE) int size
    ) {
        log.info("Requested all events by parameters: text - {}, categories - {}, paid - {}, rangeStart - {}, " +
                "rangeEnd - {}, onlyAvailable - {}", text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        ParamObject paramObj = ParamObject.newBuilder().withText(text).withCategories(categories).withPaid(paid)
                .withRangeStart(rangeStart).withRangeEnd(rangeEnd).withOnlyAvailable(onlyAvailable).orderBy(orderBy)
                .from(from).size(size).build();
        return service.findAll(paramObj);
    }

    @GetMapping("/{id}")
    public EventFullDto findById(@PathVariable("id") @Positive long id) {
        log.info("Attempt to receive event with ID {}", id);
        return service.findById(id);
    }
}
