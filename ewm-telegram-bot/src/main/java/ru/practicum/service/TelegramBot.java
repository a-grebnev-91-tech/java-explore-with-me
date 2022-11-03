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
            String message = update.getMessage().getText();
            switch (message) {
                case START_COMMAND:
                    executeStart(update.getMessage());
                    break;
                case PUBLISHED_ALL_COMMAND:
                    executeAllPublishedCommand(update.getMessage());
                    break;
                case PUBLISHED_MY_COMMAND:
                    executeMyPublishedCommand(update.getMessage());
                    break;
                default:
                    sendMessage(update.getMessage().getChatId(), "Извините, такая команда не поддерживается!");
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
        throw new RuntimeException("not impl");
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

    private String getHelp() {
        return "Для вас доступны следующие комманды:\n" +
                PUBLISHED_ALL_COMMAND + PUBLISHED_ALL_COMMAND_TEXT +
                HELP_COMMAND + HELP_COMMAND_TEXT;
    }

    private String getHelpAuth() {
        return "Для вас доступны следующие комманды:\n" +
                PUBLISHED_ALL_COMMAND + PUBLISHED_ALL_COMMAND_TEXT +
                PUBLISHED_MY_COMMAND + PUBLISHED_MY_COMMAND_TEXT +
                HELP_COMMAND + HELP_COMMAND_TEXT;
    }

    private boolean registerUser(Message message) {
        if (repo.existsById(message.getChatId())) {
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
            answer.append(getHelp());
        } else {
            answer.append(", рады видеть вас снова!");
        }
        log.info("Replied to user " + name);
        sendMessage(chatId, answer.toString());
    }
}
