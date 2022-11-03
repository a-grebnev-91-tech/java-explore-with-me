package ru.practicum.client;

import ru.practicum.dto.EventNotification;
import ru.practicum.dto.RequestNotification;

public interface NotificationClient {
    void sendEvent(EventNotification dto);

    void sendRequest(RequestNotification dto);
}
