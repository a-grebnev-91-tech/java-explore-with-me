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
            case INCOMING_REQUESTER:
                sendEventIncomingToRequesters(dto);
                break;
            case INCOMING_INITIATOR:
                sendEventIncomingToInitiator(dto);
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

    private void sendEventIncomingToInitiator(EventNotification dto) {
        Optional<TelegramUser> maybeInitiator = repo.findByEwmId(dto.getInitiatorId());
        if (maybeInitiator.isPresent()) {
            TelegramUser initiator = maybeInitiator.get();
            if (initiator.getNotifyMyEvent()) {
                bot.sendMessage(initiator.getTelegramId(), prepareIncomingToInitiatorText(dto));
            } else {
                log.info("User with ID {} isn't subscribed to this notifications", initiator.getTelegramId());
            }
        } else {
            log.info("User with ewm ID {} isn't sign in in bot", dto.getInitiatorId());
        }
    }

    private void sendEventIncomingToRequesters(EventNotification dto) {
        List<TelegramUser> usersToNotify = repo.findAllByEwmIdIn(dto.getParticipantsIds());
        String text = prepareIncomingToParticipationText(dto);
        for (TelegramUser participation : usersToNotify) {
            if (participation.getNotifyIncoming()) {
                bot.sendMessage(participation.getTelegramId(), text);
            } else {
                log.info("User with ID {} isn't subscribed to this notifications", participation.getTelegramId());
            }
        }
    }


    private void sendEventPublishedForAllUsers(EventNotification dto) {
        List<TelegramUser> usersToNotify = repo.findAllByNotifyEventPublished(true);
        String textForAll = prepareEventPublishedText(dto);
        for (TelegramUser user : usersToNotify) {
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

    private String prepareIncomingToInitiatorText(EventNotification dto) {
        StringBuilder builder = new StringBuilder("Ваше событие ")
                .append(dto.getTitle()).append(" под номером ").append(dto.getEventId())
                .append(", которое будет проходить ")
                .append(dto.getEventDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .append(" состоится уже через сутки!");
    }

    private String prepareIncomingToParticipationText(EventNotification dto) {
        StringBuilder builder = new StringBuilder("Событие ")
                .append(dto.getTitle()).append(" под номером ").append(dto.getEventId())
                .append(", которое будет проходить ")
                .append(dto.getEventDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .append(" и в котором вы планируете принять участие состоится уже через час!");
    }
}
