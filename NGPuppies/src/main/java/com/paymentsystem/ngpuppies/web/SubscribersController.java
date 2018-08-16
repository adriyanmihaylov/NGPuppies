package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.services.base.SubscribersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


    @DeleteMapping("/delete")
    public String deleteByNumber(@RequestParam("phoneNumber") String phoneNumber, Model model) {
        String message;
        if (subscribersService.deleteByNumber(phoneNumber)) {
            message = "Subscriber " + phoneNumber+ " deleted successfully!";
        } else {
            message = "Subscriber " + phoneNumber + " was not found!";
        }
        model.addAttribute("message", message);

        return "index";
    }

}
