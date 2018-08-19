package com.paymentsystem.ngpuppies.web.RestControllers;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.services.base.SubscribersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subscriber")
public class SubscribersRestController {
    @Autowired
    private SubscribersService subscribersService;

    @GetMapping("/all")
    public List<Subscriber> getAll() {
        return subscribersService.getAll();
    }

    @GetMapping("/get")
    public Subscriber getByNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return subscribersService.getByNumber(phoneNumber);
    }

    @PostMapping("/create")
    public void createSubscriber(@RequestParam(name="phoneNumber") String phoneNumber,
                                 @RequestParam(name = "firstName") String firstName,
                                 @RequestParam(name = "lastName") String lastName,
                                 @RequestParam(name = "egn") String egn,
                                 @RequestParam(name = "address", required = false) String address,
                                 @RequestParam(name = "client") String clientUsername) {

        if (subscribersService.checkIfPhoneExists(phoneNumber)) {
            System.out.println("Subscriber exists");
        }else{
            Subscriber subscriberToCreate = new Subscriber(phoneNumber, firstName, lastName, egn, clientUsername);
            if(subscribersService.create(subscriberToCreate)){
                System.out.println("Susbcriber Created" + subscriberToCreate);
            }
            else{
                System.out.println("NO such client with username: " + subscriberToCreate.getClientUsername() + "or dublicated value");
            }
        }
    }

    @DeleteMapping("/delete")
    public void  deleteByNumber(@RequestParam("phoneNumber") String phoneNumber) {
        String message;
        if (subscribersService.deleteByNumber(phoneNumber)) {
            message = "Subscriber " + phoneNumber+ " deleted successfully!";
        } else {
            message = "Subscriber " + phoneNumber + " was not found!";
        }
        System.out.println(message);
    }

}
