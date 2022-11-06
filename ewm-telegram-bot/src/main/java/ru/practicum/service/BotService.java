package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventNotification;
import ru.practicum.dto.RequestNotification;
import ru.practicum.entity.TelegramUser;
import ru.practicum.exception.NotFoundException;
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
        Optional<TelegramUser> maybeUser = repo.findByEwmId(ewmId);
        boolean changedTelegram = false;
        if (maybeUser.isEmpty()) {
            maybeUser = repo.findById(tgId);
            changedTelegram = true;
        }
        if (maybeUser.isPresent()) {
            TelegramUser user = maybeUser.get();
            if (changedTelegram) {
                repo.delete(user);
                user.setTelegramId(tgId);
                repo.save(user);
            } else {
                user.setEwmId(ewmId);
                repo.save(user);
            }
        } else {
            throw new NotFoundException("User not found", String.format("User with telegram ID %d isn't exist", tgId));
        }
    }

    public void deleteTelegram(long ewmId) {
        Optional<TelegramUser> maybeUser = repo.findByEwmId(ewmId);
        if (maybeUser.isPresent()) {
            TelegramUser user = maybeUser.get();
            repo.delete(user);
        } else {
            throw new NotFoundException("User not found", String.format("User with ewm ID %d isn't exist", ewmId));
        }
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

    //todo`
    public void sendRequestNotification(RequestNotification dto) {
        throw new RuntimeException("not impl");
    }

    private void sendEventCanceled(EventNotification dto) {
        Optional<TelegramUser> maybeInitiator = repo.findByEwmId(dto.getInitiatorId());
        if (maybeInitiator.isPresent()) {
            TelegramUser initiator = maybeInitiator.get();
            if (initiator.isNotifyMyEvent()) {
                bot.sendMessage(initiator.getTelegramId(), prepareCanceledText(dto));
            } else {
                log.info("User with ID {} isn't subscribed to this notifications", initiator.getTelegramId());
            }
        } else {
            log.info("User with ewm ID {} isn't sign in in bot", dto.getInitiatorId());
        }
    }

    private void sendEventIncomingToInitiator(EventNotification dto) {
        Optional<TelegramUser> maybeInitiator = repo.findByEwmId(dto.getInitiatorId());
        if (maybeInitiator.isPresent()) {
            TelegramUser initiator = maybeInitiator.get();
            if (initiator.isNotifyMyEvent()) {
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
            if (participation.isNotifyIncoming()) {
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

    private String prepareCanceledText(EventNotification event) {
        return "Ваше событие " +
                event.getTitle() + " под номером " + event.getEventId() +
                ", которое было запланировано на " +
                event.getEventDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)) +
                ", отменено.";
    }

    private String prepareEventPublishedInitiatorText(EventNotification event) {
        return "Ваше событие " +
                event.getTitle() + " под номером " + event.getEventId() +
                ", которое будет проходить " +
                event.getEventDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)) +
                ", одобрено и опубликованно администратором";
    }

    private String prepareEventPublishedText(EventNotification event) {
        return "Опубликовано новое событие!\n\"" + event.getTitle() + "\" которое будет проходить "
                + event.getEventDate() + ".\nСпешите принять участие!";
    }

    private String prepareIncomingToInitiatorText(EventNotification dto) {
        return "Ваше событие " +
                dto.getTitle() + " под номером " + dto.getEventId() +
                ", которое будет проходить " +
                dto.getEventDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)) +
                " состоится уже через сутки!";
    }

    private String prepareIncomingToParticipationText(EventNotification dto) {
        return "Событие " +
                dto.getTitle() + " под номером " + dto.getEventId() +
                ", которое будет проходить " +
                dto.getEventDate().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)) +
                " и в котором вы планируете принять участие состоится уже через час!";
    }
}
