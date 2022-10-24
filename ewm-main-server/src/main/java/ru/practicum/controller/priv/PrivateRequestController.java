package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class PrivateRequestController {
    private final RequestService service;

    //TODO
    //если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
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
    public List<ParticipationRequestDto> findParticipationRequests(
            @PathVariable("userId") @Positive long userId,
            @PathVariable("eventId") @Positive long eventId
    ) {
        log.info("User with ID {} attempt to get his participation requests on event with ID {}", userId, eventId);
        return service.findParticipationRequests(userId, eventId);
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
