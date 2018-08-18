package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.models.ApplicationUser;
import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.services.base.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private GenericUserService<ApplicationUser> applicationUserService;
    @Autowired
    private GenericUserService<Admin> adminService;
    @Autowired
    private GenericUserService<Client> clientService;

    @GetMapping("/user")
    public String getUserByUsername(@RequestParam String username,Model model) {
        model.addAttribute("view", "test/testResults");
        model.addAttribute("user", applicationUserService.getByUsername(username));

        return "index";
    }

    @GetMapping("/admin")
    public String getAdminByUsername(@RequestParam String username,Model model) {
        model.addAttribute("view", "test/testResults");
        model.addAttribute("admin", adminService.getByUsername(username));

        return "index";
    }

    @GetMapping("/client")
    public String getClientByUsername(@RequestParam String username,Model model) {
        model.addAttribute("view", "test/testResults");
        model.addAttribute("client", clientService.getByUsername(username));

        return "index";
    }
}
