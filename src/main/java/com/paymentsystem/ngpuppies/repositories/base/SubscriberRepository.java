package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Subscriber;

import java.sql.SQLException;
import java.util.List;

public interface SubscriberRepository {
    Subscriber getByNumber(String phoneNumber);

    List<Subscriber> getAll();

    boolean create(Subscriber subscriber) throws SQLException;

    boolean update(Subscriber updatedSubscriber) throws SQLException;

    boolean delete(Subscriber subscriber);

    List<Subscriber> getAllSubscribersByService(Integer serviceId) ;

    List<Subscriber> getTenAllTimeSubscribersWithBiggestBillsPaid(Integer clientId);

    Double getSubscriberAverageInvoiceSumPaid(Integer subscriberId, String fromDate, String toDate);

    Object[] getSubscriberWithBiggestAmountPaid(Integer clientId, String fromDate, String toDate);
}