package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Subscriber;

public class SubscriberSimpleViewModel {
    public String phoneNumber;

    public String name;

    public static SubscriberSimpleViewModel fromModel(Subscriber subscriber) {
        if (subscriber != null) {
            SubscriberSimpleViewModel viewModel = new SubscriberSimpleViewModel();
            viewModel.phoneNumber = subscriber.getPhone();
            viewModel.name = subscriber.getFirstName() + " " + subscriber.getLastName();

            return viewModel;
        }
        return null;
    }
}
