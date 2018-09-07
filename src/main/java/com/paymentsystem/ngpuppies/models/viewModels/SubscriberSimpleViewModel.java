package com.paymentsystem.ngpuppies.models.viewModels;

import com.paymentsystem.ngpuppies.models.Subscriber;

public class SubscriberSimpleViewModel {
    public int id;

    public String phoneNumber = "(+359) ";

    public String name;

    public static SubscriberSimpleViewModel fromModel(Subscriber subscriber) {
        if (subscriber != null) {
            SubscriberSimpleViewModel viewModel = new SubscriberSimpleViewModel();
            viewModel.id = subscriber.getId();
            viewModel.phoneNumber += subscriber.getPhone();
            viewModel.name = subscriber.getFirstName() + " " + subscriber.getLastName();

            return viewModel;
        }
        return null;
    }
}
