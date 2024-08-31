package ru.practicum.ewm.service.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException e) {
        return generateResponse(HttpStatus.BAD_REQUEST,
                                e.getMessage(),
                                "Неправильный запрос",
                                Arrays.asList(e.getStackTrace()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleNotFoundException(final NotFoundException e) {
        return generateResponse(HttpStatus.NOT_FOUND,
                                e.getMessage(),
                                "Запрошенный экземпляр не найден",
                                Arrays.asList(e.getStackTrace()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return generateResponse(HttpStatus.BAD_REQUEST,
                                e.getAllErrors().getFirst().getDefaultMessage(),
                                "Параметр метода не прошел валидацию",
                                e.getAllErrors());
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConflictException(ConflictException e) {
        return generateResponse(HttpStatus.CONFLICT, e.getMessage(), e.getReason(), Arrays.asList(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
        return generateResponse(HttpStatus.CONFLICT,
                                e.getErrorMessage(),
                                "Unique constraint violation",
                                Arrays.asList(e.getMessage()));
    }

    private ResponseEntity<Object> generateResponse(HttpStatus status,
                                                    String message,
                                                    String reason,
                                                    List<? extends Object> errors) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("timestamp", Instant.now());
            map.put("status", status.value());
            map.put("message", message);
            map.put("reason", reason);
            map.put("errors", errors);
        } catch (Exception e) {
            map.clear();
            map.put("timestamp", Instant.now());
            map.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            map.put("message", e.getMessage());
            map.put("reason", "Ошибка при формировании ответа");
            map.put("errors", e.getStackTrace());
        }
        return new ResponseEntity<Object>(map, status);
    }

}