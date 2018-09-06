package com.paymentsystem.ngpuppies.validation.validators;

import com.paymentsystem.ngpuppies.validation.anotations.ValidMoneyAmount;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoneyAmountValidator implements ConstraintValidator<ValidMoneyAmount, String> {
    private Pattern pattern;
    private Matcher matcher;
    private static final String MONEY_PATTERN = "^\\d*(\\.\\d{1,2})?$";

    @Override
    public void initialize(ValidMoneyAmount constraintAnnotation) {
    }

    @Override
    public boolean isValid(String moneyAmount, ConstraintValidatorContext context) {
        return (validateAmount(moneyAmount));
    }

    private boolean validateAmount(String moneyAmount) {
        if (moneyAmount == null) {
            return false;
        }
        pattern = Pattern.compile(MONEY_PATTERN);
        matcher = pattern.matcher(moneyAmount);
        return matcher.matches();
    }
}