package com.paymentsystem.ngpuppies.validation.validators;

import com.paymentsystem.ngpuppies.validation.anotations.ValidEik;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EikValidator implements ConstraintValidator<ValidEik, String> {
    private static final String EIK_PATTERN = "^[0-9]{9}$";

    @Override
    public void initialize(ValidEik constraintAnnotation) {
    }

    @Override
    public boolean isValid(String eik, ConstraintValidatorContext context) {
        return (validateEik(eik));
    }

    private boolean validateEik(String eik) {
        if (eik == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EIK_PATTERN);
        Matcher matcher = pattern.matcher(eik);

        return matcher.matches();
    }
}