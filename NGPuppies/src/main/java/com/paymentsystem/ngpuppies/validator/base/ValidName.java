package com.paymentsystem.ngpuppies.validator.base;

import com.paymentsystem.ngpuppies.validator.NameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE,PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = NameValidator.class)
@Documented
public @interface ValidName {
    String message() default "Invalid name!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}