package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.users.Client;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SubscriberViewModel {
    public int id;

    public String phoneNumber;

    public String firstName;

    public String EGN;

    public Address address;

    public ClientViewModel client;

    public Set<BillingRecordSimpleViewModel> billingRecordList;


    public static SubscriberViewModel fromModel(Subscriber subscriber) {
        SubscriberViewModel vm = new SubscriberViewModel();

        vm.id = subscriber.getId();
        vm.phoneNumber = subscriber.getPhoneNumber();
        vm.firstName = subscriber.getFirstName();
        vm.EGN = subscriber.getEgn();
        vm.address = subscriber.getAddress();
        vm.client = ClientViewModel.fromModel(subscriber.getClient());

        vm.billingRecordList = subscriber.getBillingRecords()
                .stream()
                .map(BillingRecordSimpleViewModel::fromModel)
                .collect(Collectors.toSet());

        return vm;
    }

}
