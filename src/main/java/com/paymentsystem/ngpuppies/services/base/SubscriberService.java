package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.rmi.AlreadyBoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SubscriberService {

    Subscriber getSubscriberByPhone(String phoneNumber);

    List<Subscriber> getAll();

    boolean create(Subscriber subscriber) throws SQLException;

    boolean update(Subscriber updatedSubscriber) throws SQLException;

    boolean delete(Subscriber subscriber);

    List<Subscriber> getAllSubscribersByService(Integer serviceId);

    List<Subscriber> getTenAllTimeSubscribersWithBiggestBillsPaid(Integer clientId);

    boolean addServiceToSubscriber(Subscriber subscriber, TelecomServ telecomServ) throws AlreadyBoundException, SQLException;

    Double getSubscriberAverageSumOfPaidInvoices(Integer subscriberId, String fromDate, String toDate);

    Map<Subscriber, Double> getSubscriberOfClientWithBiggestAmountPaid(Integer clientId, String fromDate, String toDate);
}