package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.practicum.repository.BotRepository;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final String name;
    private final String token;
    private final BotRepository repo;

    private static final String START_COMMAND = "/start";
    private static final String PUBLISHED_ALL_COMMAND = "/published";
    private static final String PUBLISHED_MY_COMMAND = "/publishedmy";

    public TelegramBot(
            @Value("${bot.name}") String name,
            @Value("${bot.token}") String token,
            BotRepository repository
    ) {
        this.name = name;
        this.token = token;
        this.repo = repository;
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
            long chatId = update.getMessage().getChatId();
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

    private void executeMyPublishedCommand(Message message) {
        throw new RuntimeException("not impl");
    }

    private void executeStart(Message message) {
        throw new RuntimeException("not impl");
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = ("Привет, " + name + ", приятно познакомиться!");
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
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
}
