package com.paymentsystem.ngpuppies.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("view", "test/testResults");

        return "index";
    }
}
