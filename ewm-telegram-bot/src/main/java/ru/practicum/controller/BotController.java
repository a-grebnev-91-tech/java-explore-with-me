package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EventNotification;
import ru.practicum.service.BotService;

import javax.validation.Valid;

import static ru.practicum.util.Constants.EVENT_NOTIFICATION_API_PREFIX;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BotController {
    private final BotService service;

    @PostMapping(EVENT_NOTIFICATION_API_PREFIX)
    public void sendEventNotification(@RequestBody @Valid EventNotification dto) {
        log.info("Received event notification");
        service.sendEventNotification(dto);
    }
}
