package com.musala.drones.dto;

import lombok.Data;

@Data
public class Response {

    private String message = "Dispatcher Request Processed Successfully";
    private Object result;
    private Boolean status = false;

    public Response(Boolean status, String message, Object result) {
        this.status = status;
        this.result = result;


        this.message = (message != null) ? message : this.message + (status ? " Operation is Successful" : " Error");
    }
}
