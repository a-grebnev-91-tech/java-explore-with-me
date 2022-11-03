package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventNotification;
import ru.practicum.dto.RequestNotification;
import ru.practicum.service.BotService;

import javax.validation.Valid;

import static ru.practicum.util.Constants.EVENT_NOTIFICATION_API_PREFIX;
import static ru.practicum.util.Constants.REQUEST_NOTIFICATION_API_PREFIX;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController {
    private final BotService service;

    @PostMapping("/users/{ewmId}/telegram/{telegramId}")
    public void bindTelegram(@PathVariable("ewmId") long ewmId, @PathVariable("telegramId") long tgId) {
        log.info("User with ID {} attempt to bind telegram with ID {}", ewmId, tgId);
        service.bindTelegram(ewmId, tgId);
    }

    @DeleteMapping("/users/{ewmId}")
    public void deleteTelegram(@PathVariable("ewmId") long ewmId) {
        log.info("User with ID {} attempt to remove telegram binding", ewmId);
        service.deleteTelegram(ewmId);
    }

    @PostMapping(EVENT_NOTIFICATION_API_PREFIX)
    public void sendEventNotification(@RequestBody @Valid EventNotification dto) {
        log.info("Received event notification");
        service.sendEventNotification(dto);
    }

    @PostMapping(REQUEST_NOTIFICATION_API_PREFIX)
    public void sendRequestNotification(@RequestBody @Valid RequestNotification dto) {
        log.info("Received request notification");
        service.sendRequestNotification(dto);
    }
}
