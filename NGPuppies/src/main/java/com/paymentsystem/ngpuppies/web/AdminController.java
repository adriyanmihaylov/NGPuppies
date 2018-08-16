package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("view","test/testResults");
        model.addAttribute("admin", adminService.getAll());

        return "index";
    }

    @GetMapping("/get")
    public String getByUsername(@RequestParam("username") String username, Model model) {
        model.addAttribute("view","test/testResults");
        model.addAttribute("admin", adminService.getByUsername(username));

        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("view", "admin/registration");
        model.addAttribute("admin", new Admin());

        return "index";
    }

    @PostMapping("/register")
    public String registerAdmin(@Valid Admin admin, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "test/testResult");
            return "index";
        }

        if (!adminService.checkIfUsernameIsPresent(admin.getUsername())) {
            model.addAttribute("view", "admin/registration");
            model.addAttribute("usernameExist", true);

            return "index";
        }
        if (!adminService.checkIfEmailIsPresent(admin.getEmail())) {
            model.addAttribute("view", "admin/registration");
            model.addAttribute("emailExist", true);

            return "index";
        }

        if (adminService.create(admin)) {
            model.addAttribute("view", "test/testResults");
            model.addAttribute("registrationSuccess", true);
        } else {
            model.addAttribute("view", "test/testResult");
        }

        return "index";
    }

    @DeleteMapping("/delete")
    public String deleteByUsername(@RequestParam("username") String username, Model model) {
        String message;
        if (adminService.deleteByUsername(username)) {
            message = "User " + username + " deleted successfully!";
        } else {
            message = "User " + username + " was not found!";
        }

        model.addAttribute("message", message);

        return "index";
    }
}