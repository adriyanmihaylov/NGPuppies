package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.dto.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ResponseHandler {

    public ResponseHandler() {

    }
    public ResponseEntity<Response> returnResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(new Response(message), status);
    }

    public ResponseEntity<Response> bindingResultHandler(BindingResult bindingResult) {
        FieldError error = bindingResult.getFieldErrors().get(0);
        String message = error.getDefaultMessage();
        Response response = new Response(message);
        return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
    }
}
