package com.example.surveysystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.coyote.BadRequestException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        errorLogger.error("Внутренняя ошибка сервера: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        errorLogger.error("Внутренняя ошибка сервера: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Внутренняя ошибка сервера");
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex) {
        errorLogger.error("Неавторизованный доступ: ", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неавторизованный доступ");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
        errorLogger.error("Запрашиваемый ресурс не найден: ", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Запрашиваемый ресурс не найден");
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex) {
        errorLogger.error("Доступ запрещен: ", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Доступ запрещен");
    }

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        errorLogger.error("Неверный запрос: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный запрос");
    }
}
