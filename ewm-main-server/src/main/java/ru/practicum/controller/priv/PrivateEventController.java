package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.ParticipationRequestDto;
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

    @PostMapping
    public EventFullDto addEvent(@RequestBody @Valid NewEventDto event, @PathVariable("userId") @Positive long userId) {
        log.info("User with id {} attempt to create new event {}", userId, event);
        return service.addEvent(event, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelById(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId
    ) {
        log.info("User with ID {} attempt to cancel his event with ID {}", userId, eventId);
        //TODO cancel only PENDING
        return service.cancelById(userId, eventId);
    }

    //TODO
    //если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
    //нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
    //если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId,
            @PathVariable("reqId") @Positive long reqId
    ) {
        log.info("User with ID {} attempt to reject request with ID {} for event with ID {}", userId, eventId, reqId);
        return service.confirmRequest(userId, eventId, reqId);
    }

    @GetMapping("/{eventId}")
    public EventFullDto findById(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId
    ) {
        log.info("User with ID {} attempt to get his event with ID {}", userId, eventId);
        return service.findById(userId, eventId);
    }

    @GetMapping
    public List<EventShortDto> findByInitiator(
            @PathVariable("userId") @Positive long userId,
            @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(value = "size", defaultValue = "10") @Positive int size
    ) {
        log.info("User with id {} attempt to get his events", userId);
        return service.findByInitiator(userId, from, size);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findParticipationRequests(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId
    ) {
        log.info("User with ID {} attempt to get his participation requests on event with ID {}", userId, eventId);
        return service.findParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId,
            @PathVariable("reqId") @Positive long reqId
    ) {
        log.info("User with ID {} attempt to reject request with ID {} for event with ID {}", userId, eventId, reqId);
        return service.rejectRequest(userId, eventId, reqId);
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
