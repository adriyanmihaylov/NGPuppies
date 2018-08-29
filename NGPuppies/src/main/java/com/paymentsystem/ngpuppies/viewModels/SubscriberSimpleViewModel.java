package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.users.Client;

public class SubscriberSimpleViewModel {
    public int id;

    public String phoneNumber;

    public String firstName;

    public String EGN;

    public Address address;

    public Client client;

    public static SubscriberSimpleViewModel fromModel(Subscriber subscriber) {
        SubscriberSimpleViewModel vm = new SubscriberSimpleViewModel();

        vm.id = subscriber.getId();
        vm.phoneNumber = subscriber.getPhoneNumber();
        vm.firstName = subscriber.getFirstName();
        vm.EGN = subscriber.getEgn();
        vm.address = subscriber.getAddress();
        vm.client = subscriber.getClient();

        return vm;
    }
}
