package com.paymentsystem.ngpuppies.web.dto;


public class ResponseMessage {
    public String message;

    public ResponseMessage() {

    }

    public ResponseMessage(String message) {
        setMessage(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}