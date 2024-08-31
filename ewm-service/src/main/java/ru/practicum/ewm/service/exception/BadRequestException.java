package ru.practicum.ewm.service.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String m) {
        super(m);
    }
}
