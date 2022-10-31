package ru.practicum.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.Constants.DEFAULT_DATE_TIME_FORMAT;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {
    private String message;
    private String reason;
    private String status;
    @Setter(AccessLevel.PRIVATE)
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));

    public ApiError(String message, String reason, String status) {
        this.message = message;
        this.reason = reason;
        this.status = status;
    }
}
