package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.services.base.AdminService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RegistrationController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;
}
