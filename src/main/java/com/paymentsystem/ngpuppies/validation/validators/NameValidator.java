package com.paymentsystem.ngpuppies.validation.validators;

import com.paymentsystem.ngpuppies.validation.anotations.ValidName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    private static final String NAME_PATTERN = "[A-Za-z][a-zA-Z]{2,21}$";

    @Override
    public void initialize(ValidName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return (validateName(name));
    }

    private boolean validateName(String name) {
        if(name == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
