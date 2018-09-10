package com.paymentsystem.ngpuppies.validation;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateValidator {

    public DateValidator() {
    }

    public LocalDate extractDateFromString(String dateString) throws InvalidParameterException {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new InvalidParameterException("Invalid dates!");
        }
    }

    public boolean validateDates(LocalDate from, LocalDate to) throws InvalidParameterException {
        if (from.equals(to)) {
            return true;
        }

        if (from.isAfter(to)) {
            throw new InvalidParameterException("Invalid date range!");
        }

        return true;
    }
}
