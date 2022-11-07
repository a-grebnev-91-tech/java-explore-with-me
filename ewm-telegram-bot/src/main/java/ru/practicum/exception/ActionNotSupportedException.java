package ru.practicum.exception;

import lombok.Getter;

@Getter
public class ActionNotSupportedException extends RuntimeException {
    private final String reason;

    public ActionNotSupportedException(String message, String reason) {
        super(message);
        this.reason = reason;
    }
}
