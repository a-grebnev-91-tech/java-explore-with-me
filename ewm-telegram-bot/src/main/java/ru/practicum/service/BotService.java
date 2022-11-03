package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventNotification;
import ru.practicum.dto.RequestNotification;
import ru.practicum.entity.TelegramUser;
import ru.practicum.repository.BotRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static ru.practicum.util.Constants.DEFAULT_DATE_TIME_FORMAT;

@Slf4j
@Service
@RequiredArgsConstructor
public class BotService {
    private final TelegramBot bot;
    private final BotRepository repo;

    public void bindTelegram(long ewmId, long tgId) {
        throw new RuntimeException("not impl");
    }

    public void deleteTelegram(long ewmId) {
        throw new RuntimeException("not impl");
    }

    public void sendEventNotification(EventNotification dto) {
        switch (dto.getAction()) {
            case PUBLISHED:
                sendEventPublishedForInitiator(dto);
                sendEventPublishedForAllUsers(dto);
                break;
            case INCOMING:
                sendEventIncoming(dto);
                break;
            case CANCELED:
                sendEventCanceled(dto);
                break;
        }
    }

    public void sendRequestNotification(RequestNotification dto) {
        throw new RuntimeException("not impl");
    }

    private void sendEventCanceled(EventNotification dto) {
        throw new RuntimeException("not impl");
    }


    private void sendEventPublishedForAllUsers(EventNotification dto) {
        List<TelegramUser> idsToNotify = repo.findAllByNotifyEventPublished(true);
        String textForAll = prepareEventPublishedText(dto);
        for (TelegramUser user : idsToNotify) {
            if (bot.sendMessage(user.getTelegramId(), textForAll))
                log.info("Notification for user with ID {} has been sent", user.getEwmId());
        }
    }

    private void sendEventPublishedForInitiator(EventNotification dto) {
        Optional<TelegramUser> initiator = repo.findByEwmId(dto.getInitiatorId());
        if (initiator.isPresent()) {
            String textForInitiator = prepareEventPublishedInitiatorText(dto);
            if (bot.sendMessage(initiator.get().getTelegramId(), textForInitiator))
                log.info("Notification for initiator has been sent");
        }
    }

    private void sendEventIncoming(EventNotification dto) {
        throw new RuntimeException("not impl");
    }

    private String prepareEventPublishedInitiatorText(EventNotification event) {
        StringBuilder builder = new StringBuilder("Ваше событие ")
                .append(event.getTitle()).append(" под номером ").append(event.getEventId())
                .append(", которое будет проходить ")
                .append(event.getEventDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .append(", одобрено и опубликованно администратором");
        return builder.toString();
    }

    private String prepareEventPublishedText(EventNotification dto) {
        return null;
    }
}
