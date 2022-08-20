package com.musala.drones.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value = {DispatcherException.class})
    public ResponseEntity<ErrorResponse> planIttException( DispatcherException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), null),
                StringUtils.isEmpty(ex.getStatus()) ? HttpStatus.BAD_GATEWAY : ex.getStatus());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<Object> accessDeniedException(Exception ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Access Denied", details);
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public final ResponseEntity<Object> notReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse("Bad request body sent", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public final ResponseEntity<Object> badRequestException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> details = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            details.add(errorMessage);
        });

        ErrorResponse error = new ErrorResponse(details.get(0), details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
