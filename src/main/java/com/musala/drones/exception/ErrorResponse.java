package com.musala.drones.exception;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ErrorResponse {

    private boolean status;
    private Date timestamp;
    private String message;
    private List<String> errors;

    public ErrorResponse(String message, List<String> errors) {
        this.status = false;
        this.message = message;
        this.errors = errors;
        this.timestamp = new Date();
    }
}
