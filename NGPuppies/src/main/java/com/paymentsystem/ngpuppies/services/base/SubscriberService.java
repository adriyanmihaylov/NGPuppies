package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.List;
import java.util.Map;

public interface SubscriberService {
    List<Subscriber> getAll();

    Subscriber getByNumber(String phoneNumber);

    boolean create(Subscriber subscriber) throws Exception;

    boolean update(Subscriber updatedSubscriber) throws Exception;

    boolean delete(Subscriber subscriber);

    Map<Subscriber, Double> getTopTenSubscribers(Integer clientId);

    Double getSubscriberAverageSumOfPaidInvoices(Integer subscriberId, String fromDate, String toDate);
}