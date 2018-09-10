package com.paymentsystem.ngpuppies.validation.validators;

import com.paymentsystem.ngpuppies.validation.anotations.ValidEgn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EgnValidator implements ConstraintValidator<ValidEgn, String> {
    private static final String EGN_PATTERN = "^[0-9]{10}$";

    @Override
    public void initialize(ValidEgn constraintAnnotation) {
    }

    @Override
    public boolean isValid(String egn, ConstraintValidatorContext context) {
        return (validateEgn(egn));
    }

    private boolean validateEgn(String egn) {
        if(egn == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(EGN_PATTERN);
        Matcher matcher = pattern.matcher(egn);
        return matcher.matches();
    }
}