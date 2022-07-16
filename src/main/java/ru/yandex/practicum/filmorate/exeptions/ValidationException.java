package ru.yandex.practicum.filmorate.exeptions;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
