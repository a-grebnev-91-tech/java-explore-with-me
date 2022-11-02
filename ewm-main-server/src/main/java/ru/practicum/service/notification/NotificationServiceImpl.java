package ru.practicum.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.entity.Event;
import ru.practicum.entity.ParticipationRequest;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void eventCanceled(Event event) {

    }

    @Override
    public void eventPublished(Event event) {

    }

    @Override
    public void requestConfirmed(ParticipationRequest request) {

    }

    @Override
    public void requestCreated(ParticipationRequest request) {

    }

    @Override
    public void requestRejected(ParticipationRequest request) {

    }
}
