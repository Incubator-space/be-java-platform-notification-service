package com.itm.space.notificationservice.controller;

import com.itm.space.itmplatformcommonmodels.response.HttpErrorResponse;
import com.itm.space.notificationservice.exception.ConflictException;
import com.itm.space.notificationservice.exception.NotFoundException;
import com.itm.space.notificationservice.exception.PlatformException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(PlatformException.class)
    public ResponseEntity<HttpErrorResponse> handleException(PlatformException platformException) {
        int statusCode = platformException.getHttpStatus().value();
        String errorType = platformException.getHttpStatus().getReasonPhrase();
        HttpErrorResponse error = new HttpErrorResponse(statusCode, errorType, platformException.getMessage());
        return new ResponseEntity<>(error, platformException.getHttpStatus());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
        return errorMap;
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<HttpErrorResponse> handleConflictException() {
        HttpStatus status = HttpStatus.CONFLICT;
        HttpErrorResponse error = new HttpErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Просмотреть можно только свою нотификацию"
        );
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<HttpErrorResponse> handleNotFoundException() {
        HttpStatus status = HttpStatus.NOT_FOUND;
        HttpErrorResponse error = new HttpErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                "Нотификация не найдена"
        );
        return new ResponseEntity<>(error, status);
    }
}