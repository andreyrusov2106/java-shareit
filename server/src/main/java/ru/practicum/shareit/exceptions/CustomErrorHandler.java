package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Map;

@ControllerAdvice
public class CustomErrorHandler {

    @ExceptionHandler(ValidationException.class)
    public void handleValidationException(ValidationException exception,
                                          ServletWebRequest webRequest) throws IOException {
        webRequest.getResponse().sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public void handleValidationException(ResourceNotFoundException exception,
                                          ServletWebRequest webRequest) throws IOException {
        webRequest.getResponse().sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(IllegalEnumStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalEnumStateException exception) {
        return new ResponseEntity<>(
                Map.of("error", exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
