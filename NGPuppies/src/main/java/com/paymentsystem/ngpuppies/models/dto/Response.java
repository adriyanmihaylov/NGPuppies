package com.paymentsystem.ngpuppies.models.dto;


public class Response {
    public String message;

    public Response() {

    }

    public Response(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}