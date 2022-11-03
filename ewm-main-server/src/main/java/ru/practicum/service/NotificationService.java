package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventAction;
import ru.practicum.dto.EventNotification;
import ru.practicum.entity.Event;
import ru.practicum.entity.ParticipationRequest;
import ru.practicum.repository.EventRepository;
import ru.practicum.client.NotificationClient;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationClient client;
    private final EventRepository eventRepo;

    @Scheduled(fixedDelay = 5000)
    private void incoming() {
        log.info("incoming");
        client.sendEvent(new EventNotification(1L, "", "", 1L, "name", LocalDateTime.now(), EventAction.INCOMING));
    }

    public void eventCanceled(Event event) {

    }

    public void eventPublished(Event event) {

    }

    public void requestConfirmed(ParticipationRequest request) {

    }

    public void requestCreated(ParticipationRequest request) {

    }

    public void requestRejected(ParticipationRequest request) {

    }
}