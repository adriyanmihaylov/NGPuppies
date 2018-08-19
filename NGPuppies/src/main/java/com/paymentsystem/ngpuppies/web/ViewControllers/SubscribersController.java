package com.paymentsystem.ngpuppies.web.ViewControllers;


import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.services.base.SubscribersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/subscriber")
public class SubscribersController {
    @Autowired
    private SubscribersService subscribersService;

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("view","test/testResults");
        model.addAttribute("subscriber", subscribersService.getAll());
        return "index";
    }

    @GetMapping("/get")
    public String getByNumber(@RequestParam("phoneNumber") String phoneNumber, Model model) {
        model.addAttribute("view","test/testResults");
        model.addAttribute("subscriber", subscribersService.getByNumber(phoneNumber));
        return "index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("view", "subscribers/creation");
        model.addAttribute("subscriber", new Subscriber());

        return "index";
    }

    @PostMapping("/create")
    public String createSubscriber(@Valid Subscriber subscriber, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "test/testResult");
            return "index";
        }

        if (subscribersService.checkIfPhoneExists(subscriber.getPhoneNumber())) {
            model.addAttribute("view","admin/registration");
            model.addAttribute("subscribersExists", true);

            return "index";
        }
        if(subscribersService.create(subscriber)) {
            model.addAttribute("view", "test/testResults");
            model.addAttribute("creationSuccess", true);
            System.out.println("Susbcriber Created " + subscriber);
        } else {
            model.addAttribute("view", "test/testResults");
            model.addAttribute("subscribersExists", true);
            System.out.println("Subscriber was not created");
            System.out.println("NO such client with username: " + subscriber.getClientUsername());
        }
        return "index";
    }

    @DeleteMapping("/delete")
    public String deleteByNumber(@RequestParam("phoneNumber") String phoneNumber, Model model) {
        String message;
        if (subscribersService.deleteByNumber(phoneNumber)) {
            message = "Subscriber " + phoneNumber+ " deleted successfully!";
        } else {
            message = "Subscriber " + phoneNumber + " was not found!";
        }
        model.addAttribute("view","test/testResults");
        model.addAttribute("message", message);

        return "index";
    }

}
