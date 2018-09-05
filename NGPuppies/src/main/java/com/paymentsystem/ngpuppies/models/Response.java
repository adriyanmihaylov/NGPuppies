package com.paymentsystem.ngpuppies.models;


public class Response {
    public String message;

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
