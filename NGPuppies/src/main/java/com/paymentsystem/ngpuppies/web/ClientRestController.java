package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${common.basepath}/clients")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class ClientRestController {

    @Autowired
    private ClientService clientService;
}