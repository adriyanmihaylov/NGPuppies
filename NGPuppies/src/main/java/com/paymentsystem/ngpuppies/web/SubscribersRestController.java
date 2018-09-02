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
}
