package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.practicum.entity.TelegramUser;
import ru.practicum.mapper.UserMapper;
import ru.practicum.repository.BotRepository;

import java.util.Optional;

import static ru.practicum.util.Commands.*;

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
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            long chatId = message.getChatId();
            switch (message.getText()) {
                case START_COMMAND:
                    executeStart(update.getMessage());
                    break;
                case HELP_COMMAND:
                    if (isAuthInApp(chatId)) {
                        sendMessage(chatId, getHelpAuthMsg());
                    } else {
                        sendMessage(chatId, getHelpMsg());
                    }
                    break;
                case PUBLISHED_ALL_COMMAND:
                    executeAllPublishedCommand(update.getMessage());
                    break;
                case PUBLISHED_MY_COMMAND:
                    executeMyPublishedCommand(update.getMessage());
                    break;
                default:
                    sendMessage(chatId, COMMAND_NOT_SUPPORTED_MESSAGE);
            }
        }
    }

    public boolean sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return executeMessage(message);
    }

    private void executeAllPublishedCommand(Message message) {
        long chatId = message.getChatId();
        Optional<TelegramUser> mayBeUser = repo.findById(chatId);
        if (mayBeUser.isPresent()) {
            TelegramUser user = mayBeUser.get();
            if (user.getNotifyEventPublished()) {
                sendMessage(chatId, ALREADY_SUBSCRIBED_MESSAGE);
                log.info("User with telegram ID {} already subscribed to event publishing notification", chatId);
                return;
            }
            user.setNotifyEventPublished(true);
            sendMessage(chatId, SUBSCRIBE_EVENT_PUBLISHED_MESSAGE);
            log.info("User with telegram ID {} subscribed to event publishing notification", chatId);
        } else {
            sendMessage(chatId, NO_AUTH_MESSAGE);
        }
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

    private void executeMyPublishedCommand(Message message) {
        throw new RuntimeException("not impl");
    }

    private void executeStart(Message message) {
        startCommandReceived(message.getChatId(), message.getFrom().getFirstName(), registerUser(message));
    }

    private String getHelpMsg() {
        return AVAILABLE_COMMANDS_MESSAGE +
                PUBLISHED_ALL_COMMAND + PUBLISHED_ALL_COMMAND_MESSAGE +
                HELP_COMMAND + HELP_COMMAND_MESSAGE;
    }

    private String getHelpAuthMsg() {
        return AVAILABLE_COMMANDS_MESSAGE +
                PUBLISHED_ALL_COMMAND + PUBLISHED_ALL_COMMAND_MESSAGE +
                PUBLISHED_MY_COMMAND + PUBLISHED_MY_COMMAND_MESSAGE +
                HELP_COMMAND + HELP_COMMAND_MESSAGE;
    }

    private boolean isAuthInApp(long chatId) {
        Optional<TelegramUser> user = repo.findById(chatId);
        if (user.isEmpty()) return false;
        return user.get().getEwmId() != null;
    }

    private boolean isAuthInBot(long chatId) {
        return repo.existsById(chatId);
    }

    private boolean registerUser(Message message) {
        if (isAuthInBot(message.getChatId())) {
            TelegramUser user = mapper.mapToUser(message.getFrom());
            repo.save(user);
            log.info("User with ID {} saved", user.getTelegramId());
            return true;
        } else {
            return false;
        }
    }

    private void startCommandReceived(long chatId, String name, boolean isNew) {
        StringBuilder answer = new StringBuilder();
        answer.append("Привет, ").append(name);
        if (isNew) {
            answer.append(", приятно познакомиться!");
            answer.append(System.lineSeparator());
            answer.append(getHelpMsg());
        } else {
            answer.append(", рады видеть вас снова!");
        }
        log.info("Replied to user " + name);
        sendMessage(chatId, answer.toString());
    }
}
