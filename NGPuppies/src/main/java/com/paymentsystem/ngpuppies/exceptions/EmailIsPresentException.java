package com.paymentsystem.ngpuppies.exceptions;

import org.hibernate.JDBCException;

import java.sql.SQLException;

public class EmailIsPresentException extends JDBCException {
    public EmailIsPresentException(String message, SQLException cause) {
        super(message, cause);
    }
}