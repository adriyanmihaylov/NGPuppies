package com.paymentsystem.ngpuppies.validation.validators;

import com.paymentsystem.ngpuppies.validation.anotations.ValidDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class DateValidator implements ConstraintValidator<ValidDate, String> {

    private static final String DATE_PATTERN = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";

    @Override
    public void initialize(ValidDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        return (validateDate(date));
    }

    private boolean validateDate(String date) {
        if (date == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }
}