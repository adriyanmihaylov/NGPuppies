package com.paymentsystem.ngpuppies.validation.validators;

import com.paymentsystem.ngpuppies.validation.anotations.ValidServiceName;

import javax.validation.*;
import java.util.regex.*;

public class ServiceNameValidator implements ConstraintValidator<ValidServiceName, String> {
    private Pattern pattern;
    private Matcher matcher;

    private static final String SERVICE_NAME_PATTERN = "^[A-z0-9]*((-|\\s)*[_A-z0-9]){2,20}$";

    @Override
    public void initialize(ValidServiceName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String serviceName, ConstraintValidatorContext context) {
        return (validateName(serviceName));
    }

    private boolean validateName(String serviceName) {
        if (serviceName == null) {
            return false;
        }
        pattern = Pattern.compile(SERVICE_NAME_PATTERN);
        matcher = pattern.matcher(serviceName);
        return matcher.matches();
    }
}