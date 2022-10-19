package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> findByInitiator(
            @PathVariable("userId") @Positive long userId,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        log.info("User with id {} attempt to get his events", userId);
        return service.findByInitiator(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateByInitiator(
            @PathVariable("userId") @Positive long userId,
            @RequestBody @Valid UpdateEventRequest dto
    ) {
        log.info("User with id {} attempt to update event with id {}", userId, dto.getEventId());
        return service.updateByInitiator(userId, dto);
    }
}
