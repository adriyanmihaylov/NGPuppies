package com.paymentsystem.ngpuppies.exceptions;

import org.hibernate.JDBCException;

import java.sql.SQLException;

public class UsernameIsPresentException extends Throwable {
    public UsernameIsPresentException(String message, Throwable cause) {
        super(message, cause);
    }
}
