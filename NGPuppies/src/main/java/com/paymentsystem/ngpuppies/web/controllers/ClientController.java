package com.paymentsystem.ngpuppies.web.controllers;

import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.services.base.BillingService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/secured/client")
//@PreAuthorize("hasRole('CLIENT')")
public class ClientController {

    private final SubscriberService subscriberService;
    private final BillingService billingService;

    @Autowired
    public ClientController(SubscriberService subscriberService, BillingService billingService) {
        this.subscriberService = subscriberService;
        this.billingService = billingService;
    }

    @GetMapping("/subscriber/register")
    public String create(Model model) {
        model.addAttribute("view","sections/subscriber-registration");
        model.addAttribute("subscriber", new Subscriber());

        return "index";
    }

    @PostMapping("/subscriber/register")
    public String createSubscriber(@Valid Subscriber subscriber, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("view", "test/testResult");
            return "index";
        }

        if (subscriberService.checkIfPhoneExists(subscriber.getPhoneNumber())) {
            model.addAttribute("view","admin/registration");
            model.addAttribute("subscribersExists", true);

            return "index";
        }
        model.addAttribute("view", "test/testResults");
        if(subscriberService.create(subscriber)) {
            model.addAttribute("creationSuccess", true);
        } else {
            model.addAttribute("creationNotSuccess", true);
        }

        return "index";
    }
}
