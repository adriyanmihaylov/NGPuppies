package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("admin", adminService.getAll());

        return "index";
    }
}