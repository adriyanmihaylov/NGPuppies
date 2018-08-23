package com.paymentsystem.ngpuppies.web.controllers;

import com.paymentsystem.ngpuppies.models.users.ApplicationUser;
import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
import com.paymentsystem.ngpuppies.services.ApplicationUserServiceImpl;
import com.paymentsystem.ngpuppies.services.ClientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ApplicationUserServiceImpl applicationUserService;
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("view","admin/login");
        model.addAttribute("user", new ApplicationUser());
        return "index";
    }
}