package ru.practicum.util;

import lombok.Getter;

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
}
