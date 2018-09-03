package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;

public class SubscriberSimpleViewModel {
    public int id;

    public String phoneNumber;

    public String firstName;

    public String EGN;

    public Address address;

    public ClientViewModel client;

    public static SubscriberSimpleViewModel fromModel(Subscriber subscriber) {
        SubscriberSimpleViewModel vm = new SubscriberSimpleViewModel();

        vm.id = subscriber.getId();
        vm.phoneNumber = subscriber.getPhone();
        vm.firstName = subscriber.getFirstName();
        vm.EGN = subscriber.getEgn();
        vm.address = subscriber.getAddress();
        vm.client = ClientViewModel.fromModel(subscriber.getClient());

        return vm;
    }
}
