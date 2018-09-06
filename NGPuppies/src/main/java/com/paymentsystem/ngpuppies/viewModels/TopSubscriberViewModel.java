package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Subscriber;

public class TopSubscriberViewModel {
    public int id;

    public String phoneNumber;

    public String firstName;
    public String lastName;
    public double totalAmount;


    public static TopSubscriberViewModel fromModel(Subscriber subscriber) {
        TopSubscriberViewModel vm = new TopSubscriberViewModel();

        if (subscriber != null) {
            vm.id = subscriber.getId();
            vm.phoneNumber = subscriber.getPhone();
            vm.firstName = subscriber.getFirstName();
            vm.totalAmount = subscriber.getTotalAmount();
            vm.lastName = subscriber.getLastName();
            return vm;
        }

        return null;
    }
}
