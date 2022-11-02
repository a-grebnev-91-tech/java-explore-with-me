package ru.practicum.service.notification;

import ru.practicum.entity.Event;
import ru.practicum.entity.ParticipationRequest;

public interface NotificationService {
    void eventCanceled(Event event);

    void eventPublished(Event event);

    void requestConfirmed(ParticipationRequest request);

    void requestCreated(ParticipationRequest request);

    void requestRejected(ParticipationRequest request);
}
