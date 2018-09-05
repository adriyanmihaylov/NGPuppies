package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.datatransferobjects.ClientDto;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${common.basepath}/clients")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

}