package ru.practicum.util;

public class Commands {
    public static final String ALREADY_SUBSCRIBED_MESSAGE = "Похоже вы уже подписаны на это оповещение";
    public static final String AVAILABLE_COMMANDS_MESSAGE = "Для вас доступны следующие команды:\n";
    public static final String COMMAND_NOT_SUPPORTED_MESSAGE = "Извините, такая команда не поддерживается!";
    public static final String HELP_COMMAND = "/help";
    public static final String HELP_COMMAND_MESSAGE = " - для вывода данной справки.";
    public static final String PUBLISHED_ALL_COMMAND = "/published";
    public static final String PUBLISHED_ALL_COMMAND_MESSAGE
            = " - для получения уведомлений о публикации новых событий;\n";
    public static final String PUBLISHED_MY_COMMAND = "/publishedmy";
    public static final String PUBLISHED_MY_COMMAND_MESSAGE = " - для получения уведомлений о публикации ваших событий\n";
    public static final String START_COMMAND = "/start";
    public static final String SUBSCRIBE_NEW_EVENT_PUBLISHED_MESSAGE = "Теперь вы будете получать уведомления " +
            "о публикации новых событий!";
    public static final String SUBSCRIBE_MY_EVENT_PUBLISHED_MESSAGE = "Теперь вы будете получать уведомления " +
            "о публикации ваших событий!";
    public static final String NO_AUTH_MESSAGE = "Извините, похоже вы не авторизованы. Для авторизации воспользуйтесь" +
            " командой: " + START_COMMAND;
    public static final String NO_AUTH_EWM_APP_MESSAGE = "Извините, похоже вы не авторизованы, в приложении " +
            " \"Explore With Me!\". Для авторизации воспользуйтесь официальным сайтом приложения";
}
