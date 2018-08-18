package com.paymentsystem.ngpuppies.web.uiControllers;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.models.ApplicationUser;
import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
import com.paymentsystem.ngpuppies.services.ApplicationUserServiceImpl;
import com.paymentsystem.ngpuppies.services.ClientServiceImpl;
import com.paymentsystem.ngpuppies.services.base.GenericUserService;
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