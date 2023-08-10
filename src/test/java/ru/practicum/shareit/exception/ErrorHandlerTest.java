package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTest {

    @InjectMocks
    private ErrorHandler errorHandler;

    @Test
    public void handleUnsupportedStateExceptionTest() {
        UnsupportedStateException exception = new UnsupportedStateException("аааа");
        ResponseEntity<Response> responseEntity = errorHandler.handleUnsupportedStateException();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Unknown state: UNSUPPORTED_STATUS", responseEntity.getBody().getError());
    }
}