package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Subscriber;

import java.sql.SQLException;
import java.util.List;

public interface SubscriberRepository {
    List<Subscriber> getAll();

    Subscriber getByNumber(String phoneNumber);

    boolean create(Subscriber subscriber) throws Exception;

    boolean update(Subscriber updatedSubscriber) throws Exception;

    boolean delete(Subscriber subscriber);
}