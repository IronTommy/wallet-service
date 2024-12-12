package com.example.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Глобальный обработчик исключений для контроллеров.
 * Обрабатывает все исключения и возвращает соответствующие ответы.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка ошибок валидации входящих данных.
     *
     * @param ex исключение валидации.
     * @return ответ с ошибкой валидации.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", errorMessage));
    }

    /**
     * Общая обработка исключений.
     *
     * @param ex исключение.
     * @return ответ с ошибкой.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * Обработка ошибок чтения HTTP сообщения.
     *
     * @param ex исключение.
     * @return ответ с ошибкой формата сообщения.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Malformed JSON request");
    }

    /**
     * Обработка ошибок оптимистичного блокирования.
     *
     * @param ex исключение.
     * @return ответ с ошибкой.
     */
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("The wallet was updated by another transaction. Please try again.");
    }
}
