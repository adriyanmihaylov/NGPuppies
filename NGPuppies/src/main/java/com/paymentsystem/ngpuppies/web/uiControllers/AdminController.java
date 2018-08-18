package com.paymentsystem.ngpuppies.web.uiControllers;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.models.ApplicationUser;
import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
import com.paymentsystem.ngpuppies.services.ApplicationUserServiceImpl;
import com.paymentsystem.ngpuppies.services.ClientServiceImpl;
import com.paymentsystem.ngpuppies.services.base.GenericUserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/secured")
public class AdminController {

    @Autowired
    private ApplicationUserServiceImpl applicationUserService;
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private ClientServiceImpl clientService;

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

    @GetMapping("/admin/register")
    public String registerAdmin(Model model) {
        model.addAttribute("view","admin/registration");
        model.addAttribute("admin",new Admin());

        return "index";
    }

    @PostMapping("/admin/register")
    public String registerAdmin(@Valid  Admin admin, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        if (applicationUserService.checkIfUsernameIsPresent(admin.getUsername())) {
            model.addAttribute("view", "admin/registration");
            model.addAttribute("usernameExist", true);

            return "index";
        }
        if (adminService.checkIfEmailIsPresent(admin.getEmail())) {
            model.addAttribute("view", "admin/registration");
            model.addAttribute("emailExist", true);

            return "index";
        }

        adminService.create(admin);
        model.addAttribute("view", "test/testResults");
        model.addAttribute("registrationSuccess",true);
        return "index";
    }

    @GetMapping("/client/register")
    public String registerClient(Model model) {
        model.addAttribute("view", "client/registration");
        model.addAttribute("client", new Client());

        return "index";
    }

    @PostMapping("/client/register")
    public String registerClient(@Valid  Client client, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        if (applicationUserService.checkIfUsernameIsPresent(client.getUsername())) {
            model.addAttribute("view", "client/registration");
            model.addAttribute("usernameExist", true);

            return "index";
        }
        if (clientService.checkIfEikIsPresent(client.getEik())) {
            model.addAttribute("view", "client/registration");
            model.addAttribute("eikExist", true);

            return "index";
        }

        clientService.create(client);
        model.addAttribute("view", "test/testResults");
        model.addAttribute("registrationSuccess",true);

        return "index";
    }


    @GetMapping("/delete")
    public String deletePage(Model model) {
        model.addAttribute("view", "test/delete");
        model.addAttribute("user", new ApplicationUser());

        return "index";
    }

    @DeleteMapping("/delete")
    public String deleteByUsername(@Valid ApplicationUser user,Model model) {
        model.addAttribute("view","test/testResults");

        if (applicationUserService.deleteByUsername(user.getUsername())) {
            model.addAttribute("deleteSuccess", true);
        } else {
            model.addAttribute("deleteNotSuccess", true);
        }
        return "index";
    }
}
