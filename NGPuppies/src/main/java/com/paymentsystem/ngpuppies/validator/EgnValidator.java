package com.paymentsystem.ngpuppies.validator;

import com.paymentsystem.ngpuppies.validator.base.ValidEgn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EgnValidator implements ConstraintValidator<ValidEgn, String> {

    private Pattern pattern;
    private Matcher matcher;
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

        pattern = Pattern.compile(EGN_PATTERN);
        matcher = pattern.matcher(egn);
        return matcher.matches();
    }
}