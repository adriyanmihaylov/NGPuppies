package com.paymentsystem.ngpuppies.validator;

import com.paymentsystem.ngpuppies.validator.base.ValidEik;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EikValidator implements ConstraintValidator<ValidEik, String> {

    private Pattern pattern;
    private Matcher matcher;
    private static final String EIK_PATTERN = "^[0-9]{9}$";

    @Override
    public void initialize(ValidEik constraintAnnotation) {
    }

    @Override
    public boolean isValid(String eik, ConstraintValidatorContext context) {
        return (validateEik(eik));
    }

    private boolean validateEik(String eik) {
        pattern = Pattern.compile(EIK_PATTERN);
        matcher = pattern.matcher(eik);
        return matcher.matches();
    }
}