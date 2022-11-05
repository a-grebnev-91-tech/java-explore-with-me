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
import ru.practicum.util.Command;

import java.util.Optional;

import static ru.practicum.util.Commands.*;
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
                return getStartCommandMessage(message.getFrom().getFirstName(), isNew);
            case HELP:
                return getHelpMsg(message);
            case EVENT_ALL:
                return handleAllPublish(message);
            case EVENT_MY:
                return handleMyPublish(message);
            case PARTICIPATION_REQUEST:
                return handleParticipationRequest(message);
            case PARTICIPATION_MY:
                return handleParticipationMy(message);
        }
        return "";
    }

    private String performUnauthInBotCommand(Message message) {
        if (message.getText().equals(Command.START.getCommand())) {
            boolean isNew = registerUser(message);
            if (isNew) {
                log.info("New user with Telegram ID {} has been registered", message.getChatId());
            } else {
                log.info("User with Telegram ID {} already registered", message.getChatId());
            }
            return getStartCommandMessage(message.getFrom().getFirstName(), isNew);
        } else {
            log.info("Unauthorized user with ID {} attempt to execute \"{}\" command",
                    message.getChatId(),
                    message.getText()
            );
            return NO_AUTH_MESSAGE;
        }
    }

    public boolean sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        return executeMessage(message);
    }

    private String handleAllPublish(Message message) {
        long chatId = message.getChatId();
        Optional<TelegramUser> mayBeUser = repo.findById(chatId);
        if (mayBeUser.isPresent()) {
            TelegramUser user = mayBeUser.get();
            if (user.getNotifyEventPublished()) {
                log.info("User with telegram ID {} already notified about publication new events", chatId);
                return ALREADY_SUBSCRIBED_MESSAGE;
            }
            user.setNotifyEventPublished(true);
            log.info("User with telegram ID {} subscribed to event publishing notification", chatId);
            return SUBSCRIBE_NEW_EVENT_PUBLISHED_MESSAGE;
        } else {
            return NO_AUTH_MESSAGE;
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

    private String handleMyPublish(Message message) {
        long chatId = message.getChatId();
        Optional<TelegramUser> mayBeUser = repo.findById(chatId);
        if (mayBeUser.isPresent()) {
            TelegramUser user = mayBeUser.get();
            if (user.getNotifyMyEventPublished()) {
                log.info("User with telegram ID {} already notified about publication of his events", chatId);
                return ALREADY_SUBSCRIBED_MESSAGE;
            }
            user.setNotifyMyEventPublished(true);
            log.info("User with telegram ID {} subscribed to notification about publication his events", chatId);
            return SUBSCRIBE_MY_EVENT_PUBLISHED_MESSAGE;
        } else {
            return NO_AUTH_MESSAGE;
        }
    }

    private String getHelpMsg(Message message) {
        if (isAuthInApp(message.getChatId())) {
            //TODO add more commands
            return AVAILABLE_COMMANDS_MESSAGE +
                    PUBLISHED_ALL_COMMAND + PUBLISHED_ALL_COMMAND_MESSAGE +
                    PUBLISHED_MY_COMMAND + PUBLISHED_MY_COMMAND_MESSAGE +
                    HELP_COMMAND + HELP_COMMAND_MESSAGE;
        } else {
            return AVAILABLE_COMMANDS_MESSAGE +
                    PUBLISHED_ALL_COMMAND + PUBLISHED_ALL_COMMAND_MESSAGE +
                    HELP_COMMAND + HELP_COMMAND_MESSAGE;
        }
    }

    private boolean isAuthInApp(long chatId) {
        Optional<TelegramUser> user = repo.findById(chatId);
        if (user.isEmpty()) return false;
        return user.get().getEwmId() != null;
    }

    private boolean registerUser(Message message) {
        if (repo.existsById(message.getChatId())) {
            return false;
        } else {
            TelegramUser user = mapper.mapToUser(message.getFrom());
            repo.save(user);
            log.info("User with ID {} saved", user.getTelegramId());
            return true;
        }
    }

    private String getStartCommandMessage(String name, boolean isNew) {
        StringBuilder answer = new StringBuilder();
        answer.append("Привет, ").append(name);
        if (isNew) {
            answer.append(", приятно познакомиться!");
            answer.append(System.lineSeparator());
            answer.append(getHelpMsg());
        } else {
            answer.append(", рады видеть вас снова!");
        }
        return answer.toString();
    }
}
