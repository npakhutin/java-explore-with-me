package ru.practicum.stats.server.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String m) {
        super(m);
    }
}
