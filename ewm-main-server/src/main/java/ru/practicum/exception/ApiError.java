package ru.practicum.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.util.Constants.DATE_TIME_FORMAT_STRING;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {
    private String message;
    private String reason;
    private String status;
    @Setter(AccessLevel.PRIVATE)
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_STRING));

    public ApiError(String message, String reason, String status) {
        this.message = message;
        this.reason = reason;
        this.status = status;
    }
}
