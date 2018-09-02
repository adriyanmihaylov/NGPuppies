package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.List;

public interface SubscriberService {
    List<Subscriber> getAll();

    Subscriber getByNumber(String phoneNumber);

    boolean deleteByNumber(String phoneNumber);

    boolean update(Subscriber updatedSubscriber) throws Exception;

    boolean create(Subscriber subscriber) throws Exception;
}
