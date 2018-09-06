package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.List;
import java.util.Map;

public interface SubscriberService {
    List<Subscriber> getAll();

    Subscriber getSubscriberByPhone(String phoneNumber);

    boolean create(Subscriber subscriber) throws Exception;

    boolean update(Subscriber updatedSubscriber) throws Exception;

    boolean delete(Subscriber subscriber);

    List<Subscriber> getSubscribersByService(Integer serviceId);

    List<Subscriber> getTenAllTimeSubscribersWithBiggestBillsPaid(Integer clientId);

    Map<Subscriber, Double> getSubscriberWithBiggestAmountPaid(Integer clientId, String fromDate, String toDate);

    Double getSubscriberAverageSumOfPaidInvoices(Integer subscriberId, String fromDate, String toDate);
}