package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.List;

public interface SubscriberService {
    List<Subscriber> getAll();

    Subscriber getByNumber(String phoneNumber);

    boolean deleteByNumber(String phoneNumber);

    boolean update(Subscriber updatedSubscriber);

    boolean create(Subscriber subscriber);

    boolean checkIfPhoneExists(String phoneNumber);
}