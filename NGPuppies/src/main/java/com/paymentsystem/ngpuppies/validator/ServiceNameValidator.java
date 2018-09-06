package com.paymentsystem.ngpuppies.validator;

import com.paymentsystem.ngpuppies.validator.base.ValidServiceName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServiceNameValidator implements ConstraintValidator<ValidServiceName, String> {
    @Override
    public void initialize(ValidServiceName constraintAnnotation) {
    }

    @Override
    public boolean isValid(String serviceName, ConstraintValidatorContext context) {
        return (validateName(serviceName));
    }

    private boolean validateName(String serviceName) {
        return serviceName.length() < 15;
    }
}