package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.services.base.ApplicationUserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ApplicationUserService applicationUserService;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("view", "test/testResults");

        return "index";
    }

    @GetMapping("/allUsers")
    public String getAllApplicationUsers(Model model) {
        model.addAttribute("view", "test/testResults");
        model.addAttribute("user", applicationUserService.getAll());

        return "index";
    }
}