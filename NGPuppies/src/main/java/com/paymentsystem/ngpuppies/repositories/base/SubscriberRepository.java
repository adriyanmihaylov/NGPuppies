package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.List;
import java.util.Map;

public interface SubscriberRepository {
    List<Subscriber> getAll();

    Subscriber getByNumber(String phoneNumber);

    boolean create(Subscriber subscriber) throws Exception;

    boolean update(Subscriber updatedSubscriber) throws Exception;

    boolean delete(Subscriber subscriber);

    Object[] getTopTenSubscribers(Integer clientId);

    Double getSubscriberAverageInvoiceSumPaid(Integer subscriberId, String fromDate, String toDate);
}