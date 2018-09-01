package com.paymentsystem.ngpuppies.validator.base;

import com.paymentsystem.ngpuppies.validator.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface ValidUsername {
    String message() default "Invalid username!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}