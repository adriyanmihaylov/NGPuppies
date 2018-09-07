package com.paymentsystem.ngpuppies.validation.anotations;

import com.paymentsystem.ngpuppies.validation.validators.ServiceNameValidator;

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
@Constraint(validatedBy = ServiceNameValidator.class)
public @interface ValidServiceName {
    String message() default "Invalid service name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}