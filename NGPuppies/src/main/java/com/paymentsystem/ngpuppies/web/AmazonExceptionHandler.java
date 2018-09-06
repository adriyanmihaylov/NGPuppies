package com.paymentsystem.ngpuppies.web;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.paymentsystem.ngpuppies.models.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class AmazonExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response handleValidationException(ConstraintViolationException exception) {
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            return new Response(violation.getMessage());
        }

        return new Response("Request parameter is invalid!");
    }
}