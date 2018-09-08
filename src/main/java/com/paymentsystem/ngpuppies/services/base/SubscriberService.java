package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.dto.SubscriberDTO;

import java.rmi.AlreadyBoundException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SubscriberService {

    List<Subscriber> getAll();

    Subscriber getSubscriberByPhone(String phoneNumber);

    boolean create(SubscriberDTO subscriberDTO) throws InvalidParameterException, SQLException;

    boolean update(String phoneNumber, SubscriberDTO subscriberDTO) throws InvalidParameterException, SQLException;

    boolean deleteSubscriberByNumber(String phoneNumber) throws InvalidParameterException;

    boolean addServiceToSubscriber(String subscriberPhone, String serviceName) throws InvalidParameterException, AlreadyBoundException, SQLException;

    List<Subscriber> getAllSubscribersUsingServiceByServiceName(String serviceName) throws InvalidParameterException;

    List<Subscriber> getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(int clientId);

    Double getSubscriberAverageSumOfPaidInvoices(Subscriber subscriber, String fromDate, String toDate) throws InvalidParameterException;

    Map<Subscriber, Double> getSubscriberOfClientWithBiggestAmountPaid(int clientId, String fromDate, String toDate) throws InvalidParameterException;
}