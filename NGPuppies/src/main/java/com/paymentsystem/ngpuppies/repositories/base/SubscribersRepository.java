package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Subscriber;

import java.sql.SQLException;
import java.util.List;

public interface SubscribersRepository {
    List<Subscriber> getAll();

    Subscriber getByNumber(String phoneNumber);

    boolean deleteByNumber(String phoneNumber);

    boolean update(Subscriber updatedSubscriber);

    boolean create(Subscriber subscriber) throws Exception;
}