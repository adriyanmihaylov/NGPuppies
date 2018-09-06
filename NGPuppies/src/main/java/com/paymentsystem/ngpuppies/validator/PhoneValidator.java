package com.paymentsystem.ngpuppies.validator;

import com.paymentsystem.ngpuppies.validator.base.ValidPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private Pattern pattern;
    private Matcher matcher;
    public static final String PHONE_PATTERN = "^([\\d]{9})$";

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return (validate(phoneNumber));
    }

    public boolean validate(String phoneNumber) {
        if(phoneNumber == null) {
            return false;
        }

        pattern = Pattern.compile(PHONE_PATTERN);
        matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}