package ru.practicum.shareit.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({NotFoundException.class, UserException.class, NoAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(final Throwable e) {
        String errorName = "Error";
        String errorDescription = e.getMessage();
        log.warn("{}. {}", errorName, errorDescription);
        return new Error(errorName, errorDescription);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleValidationException(final Throwable e) {
        String errorName = "Validation Error";
        String errorDescription = e.getMessage();
        log.warn("{}. {}", errorName, errorDescription);
        return new Error(errorName, errorDescription);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleThrowable(final Throwable e) {
        String errorName = "Internal Server Error";
        String errorDescription = "произошла непредвиденная ошибка";
        log.error(errorDescription, e);
        return new Error(errorName, errorDescription);
    }
}

