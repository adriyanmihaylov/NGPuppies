package com.paymentsystem.ngpuppies.web.controllers;

import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
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
    private SubscriberService subscriberService;

    @GetMapping("/all")
    public String getAll(Model model) {
        model.addAttribute("view","test/testResults");
        model.addAttribute("subscriber", subscriberService.getAll());
        return "index";
    }

    @GetMapping("/get")
    public String getByNumber(@RequestParam("phoneNumber") String phoneNumber, Model model) {
        model.addAttribute("view","test/testResults");
        model.addAttribute("subscriber", subscriberService.getByNumber(phoneNumber));
        return "index";
    }

    @DeleteMapping("/delete")
    public String deleteByNumber(@RequestParam("phoneNumber") String phoneNumber, Model model) {
        String message;
        if (subscriberService.deleteByNumber(phoneNumber)) {
            message = "Subscriber " + phoneNumber+ " deleted successfully!";
        } else {
            message = "Subscriber " + phoneNumber + " was not found!";
        }
        model.addAttribute("view","test/testResults");
        model.addAttribute("message", message);

        return "index";
    }
    @GetMapping("/create")
    public String getByNumber(Model model) {
        model.addAttribute("view","sections/subscriber-registration");
        model.addAttribute("subscriber", new Subscriber());
        return "index";
    }


}
