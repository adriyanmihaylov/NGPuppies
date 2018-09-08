package com.paymentsystem.ngpuppies.validation.validators;

import com.paymentsystem.ngpuppies.web.dto.PasswordResetDТО;
import com.paymentsystem.ngpuppies.validation.anotations.EqualPasswords;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualPasswordsValidator implements ConstraintValidator<EqualPasswords, PasswordResetDТО> {

    @Override
    public void initialize(EqualPasswords constraint) {
    }

    @Override
    public boolean isValid(PasswordResetDТО passwordResetDТО, ConstraintValidatorContext context) {
        return passwordResetDТО.getPassword().equals(passwordResetDТО.getConfirmPassword());
    }

}