package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class SubscriberViewModel {
    public int id;

    public String phoneNumber;

    public String firstName;

    public String lastName;

    public String EGN;

    public Address address;

    public String client;

    public List<String> services = new ArrayList<>();

    public static SubscriberViewModel fromModel(Subscriber subscriber) {
        SubscriberViewModel viewModel = new SubscriberViewModel();

        if(subscriber != null) {
            viewModel.id = subscriber.getId();
            viewModel.phoneNumber = subscriber.getPhone();
            viewModel.firstName = subscriber.getFirstName();
            viewModel.lastName = subscriber.getLastName();
            viewModel.EGN = subscriber.getEgn();
            viewModel.address = subscriber.getAddress();
            viewModel.client = subscriber.getClient().getUsername();
            for(OfferedServices offeredServices : subscriber.getSubscriberServices()) {
                viewModel.services.add(offeredServices.getName());
            }
        }

        return viewModel;
    }
}