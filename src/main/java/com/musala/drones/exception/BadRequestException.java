package com.musala.drones.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException  extends RuntimeException {
    private final String message;

    public BadRequestException(String message) {
        this.message = message;
    }
}
