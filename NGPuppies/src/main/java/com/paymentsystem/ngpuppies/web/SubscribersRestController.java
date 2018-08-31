package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.viewModels.SubscriberViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${common.basepath}/subscriber")
public class SubscribersRestController {
    private final SubscriberService subscriberService;

    @Autowired
    public SubscribersRestController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @GetMapping("/all")
    public List<SubscriberViewModel> getAll() {
        return subscriberService.getAll().stream().map(SubscriberViewModel::fromModel).
                collect(Collectors.toList());
    }

    @GetMapping("/get")
    public SubscriberViewModel getByNumber(@RequestParam("phoneNumber") String phoneNumber) {
        return SubscriberViewModel.fromModel(subscriberService.getByNumber(phoneNumber));
    }

    @PostMapping("/create")
    public boolean createSubscriber(@RequestParam(name="phoneNumber") String phoneNumber,
                                 @RequestParam(name = "firstName") String firstName,
                                 @RequestParam(name = "lastName") String lastName,
                                 @RequestParam(name = "egn") String egn,
                                 @RequestParam(name = "address", required = false) String address,
                                 @RequestParam(name = "client") String clientUsername) {

        if (subscriberService.checkIfPhoneExists(phoneNumber)) {
            System.out.println("Subscriber exists");
            return false;
        }else{
            Subscriber subscriberToCreate = new Subscriber(phoneNumber, firstName, lastName, egn, clientUsername);
            if(subscriberService.create(subscriberToCreate)){
                System.out.println("Susbcriber Created" + subscriberToCreate);
                return true;
            }
            else{
                System.out.println("NO such client with username: " + subscriberToCreate.getClientUsername() + "or dublicated value");
                return false;
            }
        }
    }

    @DeleteMapping("/delete")
    public boolean  deleteByNumber(@RequestParam("phoneNumber") String phoneNumber) {
        String message;
        if (subscriberService.deleteByNumber(phoneNumber)) {
            message = "Subscriber " + phoneNumber+ " deleted successfully!";
            return true;
        } else {
            message = "Subscriber " + phoneNumber + " was not found!";
        }
        System.out.println(message);
        return false;
    }

}
