package com.paymentsystem.ngpuppies.exceptions;

public class EmailIsPresentException extends Throwable {
    public EmailIsPresentException(String message) {
        super(message);
    }
}