package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;

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
        if (subscriber != null) {
            SubscriberViewModel vm = new SubscriberViewModel();
            vm.id = subscriber.getId();
            vm.phoneNumber = subscriber.getPhone();
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

        return null;
    }
}