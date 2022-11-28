package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ApiError handleNotFound(NotFoundException ex) {
        log.warn("Not found exception has occurred. Message: {}; Reason: {}", ex.getMessage(), ex.getReason());
        return new ApiError(ex.getMessage(), ex.getReason(), "NOT_FOUND");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ActionNotSupportedException.class)
    public ApiError handleActionNotSupported(ActionNotSupportedException ex) {
        log.warn("Not found exception has occurred. Message: {}; Reason: {}", ex.getMessage(), ex.getReason());
        return new ApiError(ex.getMessage(), ex.getReason(), "NOT_FOUND");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
    })
    public ApiError handleValidationException(Exception ex) {
        log.warn("Validation exception has occurred. Message: {}; Reason: {}", ex.getMessage(), ex.getMessage());
        return new ApiError(
                ex.getMessage(),
                "Validation failed",
                "BAD_REQUEST"
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiError handleInternalError(Exception ex) {
        log.warn("Error occurred. Message: {}", ex.getMessage());
        return new ApiError(
                "Error occurred",
                ex.getMessage(),
                "INTERNAL_SERVER_ERROR"
        );
    }
}
