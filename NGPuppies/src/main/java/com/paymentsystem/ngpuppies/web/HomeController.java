package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.services.base.AdminService;
import com.paymentsystem.ngpuppies.services.base.ApplicationUserService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private ApplicationUserService applicationUserService;
    private AdminService adminService;
    private ClientService clientService;

    public HomeController(ApplicationUserService applicationUserService, AdminService adminService, ClientService clientService) {
        this.applicationUserService = applicationUserService;
        this.adminService = adminService;
        this.clientService = clientService;
    }

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

    @GetMapping("/allAdmins")
    public String getAllAdmins(Model model) {
        model.addAttribute("view", "test/testResults");
        model.addAttribute("admin", adminService.getAll());

        return "index";
    }

    @GetMapping("/allClients")
    public String getAllClients(Model model) {
        model.addAttribute("view", "test/testResults");
        model.addAttribute("client", clientService.getAll());

        return "index";
    }
}