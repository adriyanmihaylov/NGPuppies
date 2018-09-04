package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SubscriberViewModel {
    public int id;

    public String phoneNumber;

    public String firstName;

    public String EGN;

    public Address address;

    public String client;

    public double totalAmount;


    public static SubscriberViewModel fromModel(Subscriber subscriber,Double totalAmount) {
        SubscriberViewModel vm = new SubscriberViewModel();

        if (subscriber != null) {
            vm.id = subscriber.getId();
            vm.phoneNumber = subscriber.getPhone();
            vm.firstName = subscriber.getFirstName();
            vm.EGN = subscriber.getEgn();
            vm.address = subscriber.getAddress();
            vm.client = subscriber.getClient().getUsername();
            vm.totalAmount = totalAmount;

            return vm;
        }

        return null;
    }
}
