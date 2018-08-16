package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.services.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;
}
