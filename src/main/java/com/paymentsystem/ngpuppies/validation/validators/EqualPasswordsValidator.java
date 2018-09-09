package com.paymentsystem.ngpuppies.validation.validators;

import com.paymentsystem.ngpuppies.web.dto.PasswordResetDto;
import com.paymentsystem.ngpuppies.validation.anotations.EqualPasswords;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualPasswordsValidator implements ConstraintValidator<EqualPasswords, PasswordResetDto> {

    @Override
    public void initialize(EqualPasswords constraint) {
    }

    @Override
    public boolean isValid(PasswordResetDto passwordResetDto, ConstraintValidatorContext context) {
        return passwordResetDto.getPassword().equals(passwordResetDto.getConfirmPassword());
    }

}