package ru.practicum.util;

import lombok.Getter;

import java.util.List;

//TODO rm
@Getter
public enum PrivateCommand {
    EVENT_PUBLISH("/publishmy"),
    EVENT_REJECT("/eventreject"),
    PARTICIPATION_REQUEST("/requestmyevent"),
    PARTICIPATION_REJECT("/requestreject"),
    PARTICIPATION_CONFIRM("/requestconfirm");

    private final String name;

    PrivateCommand(String name) {
        this.name = name;
    }

    public static boolean isValidCommand(String command) {
        List<String> availableCommands = List.of(
                "/publishmy", "/eventreject", "/requestmyevent", "/requestreject", "/requestconfirm"
        );
        return availableCommands.contains(command);
    }

    public static PrivateCommand of(String command) {
        switch (command) {
            case "/publishmy":
                return EVENT_PUBLISH;
            case "/eventreject":
                return EVENT_REJECT;
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
