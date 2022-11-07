package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EventNotification;
import ru.practicum.dto.RequestAction;
import ru.practicum.dto.RequestNotification;
import ru.practicum.entity.TelegramUser;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.BotRepository;
import ru.practicum.util.Command;

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
        boolean changedTelegramId = true;
        if (maybeUser.isEmpty()) {
            maybeUser = repo.findById(tgId);
            changedTelegramId = false;
        }
        if (maybeUser.isPresent()) {
            TelegramUser user = maybeUser.get();
            if (changedTelegramId) {
                repo.delete(user);
                user.setTelegramId(tgId);
                repo.save(user);
            } else {
                user.setEwmId(ewmId);
                repo.save(user);
            }
            bot.sendMessage(user.getTelegramId(), prepareAuthWelcomeText());
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

    public void sendRequestNotification(RequestNotification dto) {
        switch (dto.getAction()) {
            case CREATED:
                sendRequestCreated(dto);
                break;
            case CONFIRMED:
            case REJECTED:
                sendRequestStatusChanged(dto);
                break;
        }
    }

    private void loggedUserNotFound(Long ewmId) {
        log.info("User with ewm ID {} isn't logged into the bot", ewmId);
    }

    private void sendEventCanceled(EventNotification event) {
        Optional<TelegramUser> maybeInitiator = repo.findByEwmId(event.getInitiatorId());
        if (maybeInitiator.isPresent()) {
            TelegramUser initiator = maybeInitiator.get();
            if (initiator.isNotifyMyEvent()) {
                bot.sendMessage(initiator.getTelegramId(), prepareCanceledText(event));
            } else {
                log.info("User with ID {} isn't subscribed to this notifications", initiator.getTelegramId());
            }
        } else {
            loggedUserNotFound(event.getInitiatorId());
        }
    }

    private void sendEventIncomingToInitiator(EventNotification event) {
        Optional<TelegramUser> maybeInitiator = repo.findByEwmId(event.getInitiatorId());
        if (maybeInitiator.isPresent()) {
            TelegramUser initiator = maybeInitiator.get();
            if (initiator.isNotifyMyEvent()) {
                bot.sendMessage(initiator.getTelegramId(), prepareIncomingToInitiatorText(event));
            } else {
                log.info("User with ID {} isn't subscribed to this notifications", initiator.getTelegramId());
            }
        } else {
            loggedUserNotFound(event.getInitiatorId());
        }
    }

    private void sendEventIncomingToRequesters(EventNotification event) {
        List<TelegramUser> usersToNotify = repo.findAllByEwmIdIn(event.getParticipantsIds());
        String text = prepareIncomingToParticipationText(event);
        for (TelegramUser participation : usersToNotify) {
            if (participation.isNotifyIncoming()) {
                bot.sendMessage(participation.getTelegramId(), text);
            } else {
                log.info("User with ID {} isn't subscribed to this notifications", participation.getTelegramId());
            }
        }
    }

    private void sendEventPublishedForAllUsers(EventNotification event) {
        List<TelegramUser> usersToNotify = repo.findAllByNotifyEventPublished(true);
        String textForAll = prepareEventPublishedText(event);
        for (TelegramUser user : usersToNotify) {
            if (bot.sendMessage(user.getTelegramId(), textForAll))
                log.info("Notification for user with ID {} has been sent", user.getEwmId());
        }
    }

    private void sendEventPublishedForInitiator(EventNotification event) {
        Optional<TelegramUser> initiator = repo.findByEwmId(event.getInitiatorId());
        if (initiator.isPresent()) {
            String textForInitiator = prepareEventPublishedInitiatorText(event);
            if (bot.sendMessage(initiator.get().getTelegramId(), textForInitiator))
                log.info("Notification for initiator with ID {} has been sent", event.getInitiatorId());
        }
    }

    private void sendRequestStatusChanged(RequestNotification request) {
        Optional<TelegramUser> maybeRequester = repo.findByEwmId(request.getRequesterId());
        if (maybeRequester.isPresent()) {
            TelegramUser requester = maybeRequester.get();
            if (requester.isParticipationMy()) {
                String textForRequester;
                if (request.getAction().equals(RequestAction.CONFIRMED)) {
                    textForRequester = prepareRequestConfirmedText(request);
                } else {
                    textForRequester = prepareRequestRejectedText(request);
                }
                if (bot.sendMessage(requester.getTelegramId(), textForRequester)) {
                    log.info("Notification for initiator with ID {} has been sent", requester.getTelegramId());
                }
            } else {
                log.info("User with ID {} isn't subscribed to this notifications", request.getRequesterId());
            }
        } else {
            loggedUserNotFound(request.getRequestId());
        }
    }

    private void sendRequestCreated(RequestNotification request) {
        Optional<TelegramUser> maybeInitiator = repo.findByEwmId(request.getEventInitiatorId());
        if (maybeInitiator.isPresent()) {
            TelegramUser initiator = maybeInitiator.get();
            if (initiator.isParticipationRequest()) {
                String textForInitiator = prepareRequestCreatedText(request);
                if (bot.sendMessage(initiator.getTelegramId(), textForInitiator)) {
                    log.info("Notification for initiator with ID {} has been sent", initiator.getTelegramId());
                }
            } else {
                log.info("User with ID {} isn't subscribed to this notifications", request.getEventInitiatorId());
            }
        } else {
            loggedUserNotFound(request.getEventInitiatorId());
        }
    }

    private String prepareAuthWelcomeText() {
        return "Вы привязали свой телеграм аккаунт на сайте приложения \"Explore With Me\"!\n" +
                "Теперь вам доступен расширенный функционал оповещений.\n" +
                "Для справки введите " + Command.HELP.getCommand();
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

    private String prepareRequestConfirmedText(RequestNotification request) {
        return "Ваша заявка на участие в событии " + request.getEventTitle() + " подтверждена организатором события!";
    }

    private String prepareRequestCreatedText(RequestNotification request) {
        return "Пользователь " + request.getRequesterName() + " создал заявку на участие в вашем событии \"" +
                request.getEventTitle() + "\", с номером " + request.getEventId() + ".\n" +
                request.getRequesterName() + " ждет одобрения своей заявки!";
    }

    private String prepareRequestRejectedText(RequestNotification request) {
        return "Ваша заявка на участие в событии " + request.getEventTitle() + " отклонена организатором события, " +
                "либо в виду того, что закончились свободные места.";
    }
}