package com.musala.drones.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Data
@ToString
@Slf4j
public class DispatcherException extends RuntimeException{
    private HttpStatus status;

    private Object errors;

    public DispatcherException(String message) {
        super(message);
        status = HttpStatus.BAD_REQUEST;
        log.info(message);
    }

    public DispatcherException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        log.info(message);
    }
}
