package com.paymentsystem.ngpuppies.models.viewModels;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class SubscriberViewModel {
    public int id;

    public String phoneNumber = "(+359) ";

    public String firstName;

    public String lastName;

    public String EGN;

    public double totalAmountPaid;

    public Address address;

    public String client;

    public List<String> services = new ArrayList<>();

    public static SubscriberViewModel fromModel(Subscriber subscriber) {
        SubscriberViewModel viewModel = new SubscriberViewModel();

        if(subscriber != null) {
            viewModel.id = subscriber.getId();
            viewModel.phoneNumber += subscriber.getPhone();
            viewModel.firstName = subscriber.getFirstName();
            viewModel.lastName = subscriber.getLastName();
            viewModel.EGN = subscriber.getEgn();
            viewModel.totalAmountPaid = subscriber.getTotalAmount();
            viewModel.address = subscriber.getAddress();
            viewModel.client = subscriber.getClient().getUsername();

            for(OfferedServices offeredServices : subscriber.getSubscriberServices()) {
                viewModel.services.add(offeredServices.getName());
            }
        }

        return viewModel;
    }
}