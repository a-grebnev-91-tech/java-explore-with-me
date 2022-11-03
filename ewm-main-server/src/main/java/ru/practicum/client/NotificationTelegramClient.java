package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.dto.EventNotification;
import ru.practicum.dto.RequestNotification;
import ru.practicum.exception.handler.RestTemplateResponseErrorHandler;

import static ru.practicum.util.Constants.EVENT_NOTIFICATION_API_PREFIX;
import static ru.practicum.util.Constants.REQUEST_NOTIFICATION_API_PREFIX;

@Slf4j
@Service
public class NotificationTelegramClient implements NotificationClient {
    private final RestTemplate rest;
    private final String serverUrl;

    public NotificationTelegramClient(@Value("${telegram-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.serverUrl = serverUrl;
        this.rest = builder.errorHandler(new RestTemplateResponseErrorHandler()).build();
    }

    @Override
    public void sendEvent(EventNotification dto) {
        log.info("Sending event notification for event with ID {}", dto.getEventId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<EventNotification> httpEntity = new HttpEntity<>(dto, headers);
        rest.postForObject(serverUrl + EVENT_NOTIFICATION_API_PREFIX, httpEntity, String.class);
        log.info("Stats server write statistics");
    }

    @Override
    public void sendRequest(RequestNotification dto) {
        log.info("Sending request notification for request with ID {}", dto.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RequestNotification> httpEntity = new HttpEntity<>(dto, headers);
        rest.postForObject(serverUrl + REQUEST_NOTIFICATION_API_PREFIX, httpEntity, String.class);
        log.info("Stats server write statistics");
    }
}
