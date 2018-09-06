package com.paymentsystem.ngpuppies.validation.exceptions;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ConstraintViolationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleValidationException(ConstraintViolationException exception) {
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            return new ResponseMessage(violation.getMessage());
        }

        return new ResponseMessage("Request parameter is invalid!");
    }
}