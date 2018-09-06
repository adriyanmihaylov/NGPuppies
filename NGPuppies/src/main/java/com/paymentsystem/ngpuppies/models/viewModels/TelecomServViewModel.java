package com.paymentsystem.ngpuppies.models.viewModels;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class TelecomServViewModel {

    public String service;

    public List<SubscriberSimpleViewModel> subscribers = new ArrayList<>();

    public static TelecomServViewModel fromModel(TelecomServ telecomServ) {
        TelecomServViewModel viewModel = new TelecomServViewModel();
        if (telecomServ != null) {
            viewModel.service = telecomServ.getName();
            for (Subscriber subscriber : telecomServ.getSubscribers()) {
                viewModel.subscribers.add(SubscriberSimpleViewModel.fromModel(subscriber));
            }
        }

        return viewModel;
    }
}