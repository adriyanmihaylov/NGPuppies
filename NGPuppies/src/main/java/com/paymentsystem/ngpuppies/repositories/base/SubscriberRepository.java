package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.OfferedServices;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.List;

public interface SubscriberRepository {
    List<Subscriber> getAll();

    Subscriber getByNumber(String phoneNumber);

    boolean create(Subscriber subscriber) throws Exception;

    boolean update(Subscriber updatedSubscriber) throws Exception;

    boolean delete(Subscriber subscriber);

    List<Subscriber> getSubscribersByService(Integer serviceId) ;

    List<Subscriber> getTenAllTimeSubscribersWithBiggestBillsPaid(Integer clientId);

    Double getSubscriberAverageInvoiceSumPaid(Integer subscriberId, String fromDate, String toDate);

    Object[] getSubscriberWithBiggestAmountPaid(Integer clientId, String fromDate, String toDate);
}