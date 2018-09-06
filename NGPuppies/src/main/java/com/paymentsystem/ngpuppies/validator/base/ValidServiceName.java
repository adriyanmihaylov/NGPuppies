package com.paymentsystem.ngpuppies.validator.base;

import com.paymentsystem.ngpuppies.validator.ServiceNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD,PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = ServiceNameValidator.class)
@Documented
public @interface ValidServiceName {
    String message() default "Invalid service name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}