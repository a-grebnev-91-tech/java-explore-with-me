package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventAction;
import ru.practicum.dto.EventNotification;
import ru.practicum.entity.Event;
import ru.practicum.entity.ParticipationRequest;
import ru.practicum.mapper.EventNotificationMapper;
import ru.practicum.model.EventState;
import ru.practicum.repository.EventRepository;
import ru.practicum.client.NotificationClient;
import ru.practicum.repository.RequestRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.util.Constants.SCHEDULE_DELAY;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationClient client;
    private final EventRepository eventRepo;
    private final RequestRepository requestRepo;
    private final EventNotificationMapper mapper;
    private final Duration scheduleDelay = Duration.ofMillis(SCHEDULE_DELAY);
    private final Duration eventNotifyPeriod = Duration.ofDays(1);

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

    @Scheduled(fixedDelay = SCHEDULE_DELAY)
    private void incoming() {
        log.info("Checking for incoming events for requesters");
        Collection<EventNotification> dtos = getNotificationsForRequesters();
        log.info("Checking for incoming events for initiators");
        dtos.addAll(getNotificationsForInitiators());
        for (EventNotification notification : dtos) {
            client.sendEvent(notification);
        }
    }

    private Collection<EventNotification> getNotificationsForInitiators() {
        LocalDateTime from = LocalDateTime.now().plus(eventNotifyPeriod);
        LocalDateTime to = from.plus(scheduleDelay);
        List<Event> events = eventRepo.findAllByStateAndEventDateIsBetween(EventState.PUBLISHED, from, to);
        return events.stream()
                .map(mapper::eventToNotification)
                .peek(eventNotification -> eventNotification.setAction(EventAction.INCOMING_INITIATOR))
                .collect(Collectors.toList());
    }

    private Collection<EventNotification> getNotificationsForRequesters() {
        LocalDateTime from = LocalDateTime.now();
        LocalDateTime to = from.plus(scheduleDelay);
        List<ParticipationRequest> requestsToNotify = requestRepo.findRequestsToNotify(from, to);

        Map<Long, EventNotification> notifications = new HashMap<>();
        for (ParticipationRequest request : requestsToNotify) {
            long eventId = request.getEvent().getId();
            long requesterId = request.getRequester().getId();
            if (notifications.containsKey(eventId)) {
                notifications.get(eventId).getParticipantsIds().add(requesterId);
            } else {
                Event event = request.getEvent();
                EventNotification notification = mapper.eventToNotification(event);
                notification.setAction(EventAction.INCOMING_REQUESTER);
                List<Long> participantsIds = new ArrayList<>();
                participantsIds.add(requesterId);
                notification.setParticipantsIds(participantsIds);
                notifications.put(event.getId(), notification);
            }
        }
        return notifications.values();
    }
}