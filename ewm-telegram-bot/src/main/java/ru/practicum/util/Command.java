package ru.practicum.util;

import lombok.Getter;

import java.util.List;

@Getter
public enum Command {
    START("/start"),
    HELP("/help"),
    EVENT_ALL("/publish"),
    EVENT_MY("/publishmy"),
    PARTICIPATION_REQUEST("/requestmyevent"),
    PARTICIPATION_MY("/participationmy");

    private final String command;

    Command(String command) {
        this.command = command;
    }

    public boolean isPrivate() {
        List<Command> privateCommands = List.of(
                EVENT_MY, PARTICIPATION_MY, PARTICIPATION_REQUEST
        );
        return privateCommands.contains(this);
    }

    public static boolean isValidCommand(String command) {
        List<String> availableCommands = List.of(
                "/start", "/help", "/publish", "/publishmy", "/requestmyevent", "/participationmy");
        return availableCommands.contains(command);
    }

    public static Command of(String command) {
        switch (command) {
            case "/start":
                return START;
            case "/help":
                return HELP;
            case "/publish":
                return EVENT_ALL;
            case "/publishmy":
                return EVENT_MY;
            case "/requestmyevent":
                return PARTICIPATION_REQUEST;
            case "/participationmy":
                return PARTICIPATION_MY;
            default:
                throw new IllegalArgumentException();
        }
    }
}
