package ru.practicum.util;

import lombok.Getter;

@Getter
public enum PublicCommand {
    START("/start"),
    HELP("/help"),
    PUBLISH("/publish");

    private final String command;

    PublicCommand(String command) {
        this.command = command;
    }
}
