package com.paymentsystem.ngpuppies.validation.anotations;

import com.paymentsystem.ngpuppies.validation.validators.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE, FIELD,PARAMETER})
@Constraint(validatedBy = NameValidator.class)
public @interface ValidName {
    String message() default "Invalid name!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}