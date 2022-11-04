package ru.practicum.util;

import lombok.Getter;

import java.util.List;

@Getter
public enum Command {
    START("/start"),
    HELP("/help"),
    EVENT_ALL_PUBLISH("/publish"),
    EVENT_MY_PUBLISH("/publishmy"),
    EVENT_MY_REJECT("/eventreject"),
    PARTICIPATION_REQUEST("/requestmyevent"),
    PARTICIPATION_REJECT("/requestreject"),
    PARTICIPATION_CONFIRM("/requestconfirm");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public boolean isPrivate() {
        List<Command> privateCommands = List.of(
                EVENT_MY_PUBLISH, EVENT_MY_REJECT, PARTICIPATION_REJECT, PARTICIPATION_CONFIRM, PARTICIPATION_REQUEST
        );
        return privateCommands.contains(this);
    }

    public static boolean isValidCommand(String command) {
        List<String> availableCommands = List.of(
                "/start", "/help", "/publish", "/publishmy", "/eventreject",
                "/requestmyevent", "/requestreject", "/requestconfirm");
        return availableCommands.contains(command);
    }

    public static Command of(String command) {
        switch (command) {
            case "/start":
                return START;
            case "/help":
                return HELP;
            case "/publish":
                return EVENT_ALL_PUBLISH;
            case "/publishmy":
                return EVENT_MY_PUBLISH;
            case "/eventreject":
                return EVENT_MY_REJECT;
            case "/requestmyevent":
                return PARTICIPATION_REQUEST;
            case "/requestreject":
                return PARTICIPATION_REJECT;
            case "/requestconfirm":
                return PARTICIPATION_CONFIRM;
            default:
                throw new IllegalArgumentException();
        }
    }
}
