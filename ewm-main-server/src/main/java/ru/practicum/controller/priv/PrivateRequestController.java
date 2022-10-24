package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateRequestController {
    private final RequestService service;

    @PostMapping("/requests")
    public ParticipationRequestDto addRequest(
            @PathVariable("userId") @Positive long userId,
            @RequestParam("eventId") @Positive long eventId
    ) {
        log.info("User with ID {} add request to event with ID {}", userId, eventId);
        return service.addRequest(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId,
            @PathVariable("reqId") @Positive long reqId
    ) {
        log.info("User with ID {} attempt to reject request with ID {} for event with ID {}", userId, eventId, reqId);
        return service.confirmRequest(userId, eventId, reqId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> findRequestsByEvent(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId
    ) {
        log.info("Initiator with ID {} attempt to get his participation requests on event with ID {}", userId, eventId);
        return service.findRequestsByEvent(userId, eventId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> findRequestsByUser(@PathVariable("userId") @Positive long userId) {
        log.info("User with ID {} attempt to get his participation requests", userId);
        return service.findRequestsByUser(userId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId,
            @PathVariable("reqId") @Positive long reqId
    ) {
        log.info("User with ID {} attempt to reject request with ID {} for event with ID {}", userId, eventId, reqId);
        return service.rejectRequest(userId, eventId, reqId);
    }
}
