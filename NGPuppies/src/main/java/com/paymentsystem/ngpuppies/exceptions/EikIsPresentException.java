package com.paymentsystem.ngpuppies.exceptions;

import org.hibernate.JDBCException;

import java.sql.SQLException;

public class EikIsPresentException extends JDBCException {
    public EikIsPresentException(String message, SQLException cause) {
        super(message, cause);
    }
}
