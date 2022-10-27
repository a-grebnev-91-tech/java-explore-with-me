package ru.practicum.controller.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.model.EventOrderBy;
import ru.practicum.model.EventState;
import ru.practicum.service.EventService;
import ru.practicum.util.AdminEventParamObj;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.util.Constants.*;

@Slf4j
@Validated
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
}
