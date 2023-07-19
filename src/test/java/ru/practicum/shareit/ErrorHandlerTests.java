package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exeption.Error;
import ru.practicum.shareit.exeption.ErrorHandler;
import ru.practicum.shareit.exeption.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorHandlerTests {

    @Test
    public void handleNotFoundException_ReturnsErrorResponseWithHttpStatusNotFound() {
        NotFoundException exception = new NotFoundException("Not found");
        ErrorHandler errorHandler = new ErrorHandler();
        Error response = errorHandler.handleNotFoundException(exception);
        assertEquals("Error", response.getName());
    }

    @Test
    public void handleValidationException_ReturnsErrorResponseWithHttpStatusBadRequest() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getMessage()).thenReturn("Validation failed");
        ErrorHandler errorHandler = new ErrorHandler();
        Error response = errorHandler.handleValidationException(exception);
        assertEquals("Validation Error", response.getName());
    }

    @Test
    public void handleThrowable_ReturnsErrorResponseWithHttpStatusInternalServerError() {
        Throwable throwable = new RuntimeException("Unexpected error");
        ErrorHandler errorHandler = new ErrorHandler();
        Error response = errorHandler.handleThrowable(throwable);
        assertEquals("Internal Server Error", response.getName());
    }
}

