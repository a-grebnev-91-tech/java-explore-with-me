package ru.practicum.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.AdminUpdateEventRequest;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.model.EventState;
import ru.practicum.service.EventService;
import ru.practicum.util.AdminEventParamObj;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService service;

    @GetMapping
    public List<EventFullDto> findAll(
            @RequestParam(value = "users", required = false) List<Long> users,
            @RequestParam(value = "states", required = false) List<EventState> states,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "rangeStart", required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT_STRING)
            LocalDateTime rangeStart,
            @RequestParam(value = "rangeEnd", required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT_STRING)
            LocalDateTime rangeEnd,
            @RequestParam(value = "from", defaultValue = DEFAULT_FROM_VALUE) int from,
            @RequestParam(value = "size", defaultValue = DEFAULT_SIZE_VALUE) int size
    ) {
        log.info("Admin request all events by parameters: users - {}, states - {}, categories - {}, rangeStart - {}," +
                " rangeEnd - {}", users, states, categories, rangeStart, rangeEnd);
        AdminEventParamObj paramObj = AdminEventParamObj.newBuilder().withUsers(users).withStates(states)
                .withCategories(categories).withRangeStart(rangeStart).withRangeEnd(rangeEnd).from(from).size(size)
                .build();
        return service.findAll(paramObj);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable("eventId") long id) {
        log.info("Admin attempt to publish event with ID {}", id);
        return service.publishEvent(id);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable("eventId") long id) {
        log.info("Admin attempt to reject event with ID {}", id);
        return service.rejectEvent(id);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable("eventId") long id,
            @RequestBody AdminUpdateEventRequest event
    ) {
        log.info("Admin attempt to update event with ID {}", id);
        return service.updateByAdmin(id, event);
    }
}
