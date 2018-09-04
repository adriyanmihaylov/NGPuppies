package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.Map;

public class SubscriberSimpleViewModel {
    public int id;

    public String phoneNumber;

    public String firstName;

    public String EGN;

    public Address address;

    public String client;

    public static SubscriberSimpleViewModel fromModel(Subscriber subscriber) {
        SubscriberSimpleViewModel viewModel = new SubscriberSimpleViewModel();

        if(subscriber != null) {
            viewModel.id = subscriber.getId();
            viewModel.phoneNumber = subscriber.getPhone();
            viewModel.firstName = subscriber.getFirstName();
            viewModel.EGN = subscriber.getEgn();
            viewModel.address = subscriber.getAddress();
            viewModel.client = subscriber.getClient().getUsername();
        }

        return viewModel;
    }
}
