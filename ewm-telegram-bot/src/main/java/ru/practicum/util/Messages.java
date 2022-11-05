package ru.practicum.util;

import static ru.practicum.util.Commands.START_COMMAND;

public class Messages {
    public static final String ALREADY_SUBSCRIBED_MESSAGE = "Похоже вы уже подписаны на это оповещение";
    public static final String AVAILABLE_COMMANDS_MESSAGE = "Для вас доступны следующие команды:\n";

    public static final String COMMAND_MESSAGE_HELP = " - для вывода данной справки.";
    public static final String COMMAND_MESSAGE_PUBLISHED_ALL
            = " - для получения уведомлений о публикации новых событий;\n";
    public static final String COMMAND_MESSAGE_PUBLISHED_MY
            = " - для получения уведомлений о публикации ваших событий;\n";
    public static final String COMMAND_MESSAGE_INCOMING = " - для получения уведомлений о приближающихся событиях;\n";
    public static final String COMMAND_MESSAGE_PARTICIPATION_MY = " - для получения уведомлений о новых заявках на " +
            "участие в событиях, где вы инициатор;\n";
    public static final String COMMAND_MESSAGE_PARTICIPATION_REQUEST = " - для получения уведомлений о подтверждении " +
            "или отклонении ваших заявок на участие.\n";


    public static final String COMMAND_NOT_SUPPORTED_MESSAGE = "Извините, такая команда не поддерживается!";
    public static final String NO_AUTH_MESSAGE = "Извините, похоже вы не авторизованы. Для авторизации воспользуйтесь" +
            " командой: " + START_COMMAND;
    public static final String NO_AUTH_EWM_APP_MESSAGE = "Извините, похоже вы не авторизованы, в приложении " +
            " \"Explore With Me!\". Для авторизации воспользуйтесь официальным сайтом приложения";
    public static final String SUBSCRIBE_NEW_EVENT_PUBLISHED_MESSAGE = "Теперь вы будете получать уведомления " +
            "о публикации новых событий!";
    public static final String SUBSCRIBE_MY_EVENT_PUBLISHED_MESSAGE = "Теперь вы будете получать уведомления " +
            "о публикации ваших событий!";
    public static final String SUBSCRIBE_INCOMING_EVEN_MESSAGE = "Теперь вы будете получать уведомления " +
            "о приближающихся осбытиях! Если вы инициатор этого события - вы будете уведомлены за день, если вы " +
            "участник - вы будете уведомлены за 2 часа до начала события!";
    public static final String SUBSCRIBE_PARTICIPATION_REQUEST_MESSAGE = "Теперь вы будете получать уведомления " +
            "о создании новых заявок на участие в вашем событиии!";
    public static final String SUBSCRIBE_PARTICIPATION_MY_MESSAGE = "Теперь вы будете получать уведомления " +
            "о подтверждении или отклонении ваших заявок на участие";
}
