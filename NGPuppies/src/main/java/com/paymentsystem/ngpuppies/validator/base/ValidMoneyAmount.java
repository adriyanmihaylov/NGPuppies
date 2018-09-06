package com.paymentsystem.ngpuppies.validator.base;

import com.paymentsystem.ngpuppies.validator.MoneyAmountValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE,PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = MoneyAmountValidator.class)
@Documented
public @interface ValidMoneyAmount {
    String message() default "Invalid money format!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}