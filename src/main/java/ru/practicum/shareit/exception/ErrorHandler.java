package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnsupportedStateException.class)
    protected ResponseEntity<Response> handleUnsupportedStateException() {
        log.info("Ошибка 400, ошибка статуса");
        Response response = new Response("Unknown state: UNSUPPORTED_STATUS");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}