package com.paymentsystem.ngpuppies.web.ViewControllers;

import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("view","test/testResults");
        model.addAttribute("client", clientService.getAll());

        return "index";
    }

    @GetMapping("/get")
    public String getByUsername(@RequestParam("username") String username, Model model) {
        model.addAttribute("view", "test/testResults");
        model.addAttribute("client", clientService.getByUsername(username));

        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("view", "clients/registration");
        model.addAttribute("client", new Client());

        return "index";
    }

    @PostMapping("/register")
    public String registerClient(@Valid Client client, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "test/testResult");
            return "index";
        }

        if (!clientService.checkIfUsernameIsPresent(client.getUsername())) {
            model.addAttribute("view", "clients/registration");
            model.addAttribute("usernameExist", true);

            return "index";
        }
        if (!clientService.checkIfEikIsPresent(client.getEik())) {
            model.addAttribute("view", "clients/registration");
            model.addAttribute("eikExist", true);

            return "index";
        }

        if (clientService.create(client)) {
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
        if (clientService.deleteByUsername(username)) {
            message = "Client " + username + " deleted successfully!";
        } else {
            message = "Client " + username + " was not found!";
        }

        model.addAttribute("message", message);

        return "index";
    }
}