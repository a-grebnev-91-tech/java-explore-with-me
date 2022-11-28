package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.practicum.entity.TelegramUser;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.BotRepository;
import ru.practicum.util.Command;

import java.util.Optional;

import static ru.practicum.util.Messages.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final String name;
    private final String token;
    private final BotRepository repo;
    private final UserMapper mapper;

    public TelegramBot(
            @Value("${bot.name}") String name,
            @Value("${bot.token}") String token,
            BotRepository repository,
            UserMapper mapper
    ) {
        this.name = name;
        this.token = token;
        this.repo = repository;
        this.mapper = mapper;
    }

    @Override
    public String getBotToken() {
        return this.token;
    }

    @Override
    public String getBotUsername() {
        return this.name;
    }

    @Override
    @Transactional
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String response = null;
            if (Command.isValidCommand(message.getText())) {
                Command command = Command.of(message.getText());
                response = performCommand(message, command);
            } else {
                response = COMMAND_NOT_SUPPORTED_MESSAGE;
            }
            if (sendMessage(message.getChatId(), response)) {
                log.info("Replied to user with Telegram ID {}", message.getChatId());
            }
        }
    }

    private String performCommand(Message message, Command command) {
        switch (command) {
            case START:
                boolean isNew = registerUser(message);
                return getStartCommandMessage(message.getChatId(), message.getFrom().getFirstName(), isNew);
            case HELP:
                return getHelpMsg(message.getChatId());
            case EVENT_ALL:
                return handleAllPublished(message);
            case EVENT_MY:
                return handleMyPublish(message);
            case EVENT_INCOMING:
                return handleEventIncoming(message);
            case PARTICIPATION_REQUEST:
                return handleParticipationRequest(message);
            case PARTICIPATION_MY:
                return handleParticipationMy(message);
            default:
                return COMMAND_NOT_SUPPORTED_MESSAGE;
        }
    }

    public boolean sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return executeMessage(message);
    }

    private boolean executeMessage(SendMessage message) {
        try {
            execute(message);
            return true;
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
            return false;
        }
    }

    private String getHelpMsg(long chatId) {
        if (isAuthInApp(chatId)) {
            return AVAILABLE_COMMANDS_MESSAGE +
                    Command.EVENT_ALL.getCommand() + COMMAND_MESSAGE_PUBLISHED_ALL +
                    Command.EVENT_MY.getCommand() + COMMAND_MESSAGE_PUBLISHED_MY +
                    Command.EVENT_INCOMING.getCommand() + COMMAND_MESSAGE_INCOMING +
                    Command.PARTICIPATION_MY.getCommand() + COMMAND_MESSAGE_PARTICIPATION_MY +
                    Command.PARTICIPATION_REQUEST.getCommand() + COMMAND_MESSAGE_PARTICIPATION_REQUEST +
                    Command.HELP.getCommand() + COMMAND_MESSAGE_HELP;
        } else {
            return AVAILABLE_COMMANDS_MESSAGE +
                    Command.EVENT_ALL.getCommand() + COMMAND_MESSAGE_PUBLISHED_ALL +
                    Command.HELP.getCommand() + COMMAND_MESSAGE_HELP;
        }
    }

    private String getStartCommandMessage(long chatId, String name, boolean isNew) {
        StringBuilder answer = new StringBuilder();
        answer.append("Привет, ").append(name);
        if (isNew) {
            answer.append(", приятно познакомиться!");
        } else {
            answer.append(", рады видеть вас снова!");
        }
        answer.append(System.lineSeparator());
        answer.append(getHelpMsg(chatId));
        return answer.toString();
    }

    private String handleAllPublished(Message message) {
        long chatId = message.getChatId();
        Optional<TelegramUser> mayBeUser = repo.findById(chatId);
        if (mayBeUser.isPresent()) {
            TelegramUser user = mayBeUser.get();
            if (user.isNotifyEventPublished()) {
                log.info("User with telegram ID {} already notified about publication new events", chatId);
                return ALREADY_SUBSCRIBED_MESSAGE;
            }
            user.setNotifyEventPublished(true);
            repo.save(user);
            log.info("User with telegram ID {} subscribed to event publishing notification", chatId);
            return SUBSCRIBE_NEW_EVENT_PUBLISHED_MESSAGE;
        } else {
            log.info("User with telegram ID {} isn't auth in ewm app", chatId);
            return NO_AUTH_MESSAGE;
        }
    }

    private String handleEventIncoming(Message message) {
        long chatId = message.getChatId();
        Optional<TelegramUser> mayBeUser = repo.findById(chatId);
        if (mayBeUser.isPresent()) {
            TelegramUser user = mayBeUser.get();
            if (user.getEwmId() != null) {
                if (user.isNotifyIncoming()) {
                    log.info("User with telegram ID {} already notified about incoming events", chatId);
                    return ALREADY_SUBSCRIBED_MESSAGE;
                }
                user.setNotifyIncoming(true);
                repo.save(user);
                log.info("User with telegram ID {} subscribed to notification about incoming events", chatId);
                return SUBSCRIBE_INCOMING_EVEN_MESSAGE;
            } else {
                log.info("User with telegram ID {} isn't auth in ewm app", chatId);
                return NO_AUTH_EWM_APP_MESSAGE;
            }
        } else {
            log.info("User with telegram ID {} isn't auth in bot", chatId);
            return NO_AUTH_MESSAGE;
        }
    }

    private String handleMyPublish(Message message) {
        long chatId = message.getChatId();
        Optional<TelegramUser> mayBeUser = repo.findById(chatId);
        if (mayBeUser.isPresent()) {
            TelegramUser user = mayBeUser.get();
            if (user.getEwmId() != null) {
                if (user.isNotifyMyEvent()) {
                    log.info("User with telegram ID {} already notified about publication of his events", chatId);
                    return ALREADY_SUBSCRIBED_MESSAGE;
                }
                user.setNotifyMyEvent(true);
                repo.save(user);
                log.info("User with telegram ID {} subscribed to notification about publication his events", chatId);
                return SUBSCRIBE_MY_EVENT_PUBLISHED_MESSAGE;
            } else {
                log.info("User with telegram ID {} isn't auth in ewm app", chatId);
                return NO_AUTH_EWM_APP_MESSAGE;
            }
        } else {
            log.info("User with telegram ID {} isn't auth in bot", chatId);
            return NO_AUTH_MESSAGE;
        }
    }

    private String handleParticipationMy(Message message) {
        long chatId = message.getChatId();
        Optional<TelegramUser> mayBeUser = repo.findById(chatId);
        if (mayBeUser.isPresent()) {
            TelegramUser user = mayBeUser.get();
            if (user.getEwmId() != null) {
                if (user.isParticipationMy()) {
                    log.info("User with telegram ID {} already notified about his participation requests", chatId);
                    return ALREADY_SUBSCRIBED_MESSAGE;
                }
                user.setParticipationMy(true);
                repo.save(user);
                log.info("User with telegram ID {} subscribed to notification about his participation requests", chatId);
                return SUBSCRIBE_PARTICIPATION_MY_MESSAGE;
            } else {
                log.info("User with telegram ID {} isn't auth in ewm app", chatId);
                return NO_AUTH_EWM_APP_MESSAGE;
            }
        } else {
            log.info("User with telegram ID {} isn't auth in bot", chatId);
            return NO_AUTH_MESSAGE;
        }
    }

    private String handleParticipationRequest(Message message) {
        long chatId = message.getChatId();
        Optional<TelegramUser> mayBeUser = repo.findById(chatId);
        if (mayBeUser.isPresent()) {
            TelegramUser user = mayBeUser.get();
            if (user.getEwmId() != null) {
                if (user.isParticipationRequest()) {
                    log.info("User with telegram ID {} already notified about new requests for his events", chatId);
                    return ALREADY_SUBSCRIBED_MESSAGE;
                }
                user.setParticipationRequest(true);
                repo.save(user);
                log.info("User with telegram ID {} subscribed to notification about new requests for his events",
                        chatId);
                return SUBSCRIBE_PARTICIPATION_REQUEST_MESSAGE;
            } else {
                log.info("User with telegram ID {} isn't auth in ewm app", chatId);
                return NO_AUTH_EWM_APP_MESSAGE;
            }
        } else {
            log.info("User with telegram ID {} isn't auth in bot", chatId);
            return NO_AUTH_MESSAGE;
        }
    }

    private boolean isAuthInApp(long chatId) {
        Optional<TelegramUser> user = repo.findById(chatId);
        if (user.isEmpty()) return false;
        return user.get().getEwmId() != null;
    }

    private boolean registerUser(Message message) {
        if (repo.existsById(message.getChatId())) {
            log.info("User with Telegram ID {} already registered", message.getChatId());
            return false;
        } else {
            TelegramUser user = mapper.mapToUser(message.getFrom());
            repo.save(user);
            log.info("New user with Telegram ID {} has been registered", message.getChatId());
            return true;
        }
    }
}
