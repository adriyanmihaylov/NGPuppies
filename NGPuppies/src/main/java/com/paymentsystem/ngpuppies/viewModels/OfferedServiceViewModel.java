package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class OfferedServiceViewModel {

    public String service;

    public List<SubscriberSimpleViewModel> subscribers = new ArrayList<>();

    public static OfferedServiceViewModel fromModel(OfferedServices offeredServices) {
        OfferedServiceViewModel viewModel = new OfferedServiceViewModel();
        if (offeredServices != null) {
            viewModel.service = offeredServices.getName();
            for (Subscriber subscriber : offeredServices.getSubscribers()) {
                viewModel.subscribers.add(SubscriberSimpleViewModel.fromModel(subscriber));
            }
        }

        return viewModel;
    }
}