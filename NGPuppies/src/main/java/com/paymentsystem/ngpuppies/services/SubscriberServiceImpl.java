package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    @Autowired
    private SubscriberRepository subscriberRepository;

    @Override
    public List<Subscriber> getAll() {
        return subscriberRepository.getAll();
    }

    @Override
    public Subscriber getSubscriberByPhone(String phoneNumber) {
        return subscriberRepository.getByNumber(phoneNumber);
    }

    @Override
    public boolean create(Subscriber subscriber) throws Exception {
        return subscriberRepository.create(subscriber);
    }

    @Override
    public boolean update(Subscriber updatedSubscriber) throws Exception {
        return subscriberRepository.update(updatedSubscriber);
    }

    @Override
    public boolean delete(Subscriber subscriber) {
        return subscriberRepository.delete(subscriber);
    }

    @Override
    public boolean addServiceToSubscriber(Subscriber subscriber, TelecomServ telecomServ) throws AlreadyBoundException, SQLException {
        if (subscriber.getSubscriberServices().contains(telecomServ)) {
            throw new AlreadyBoundException("The subscriber is already using this service");
        }
        subscriber.getSubscriberServices().add(telecomServ);
        if (subscriberRepository.update(subscriber)) {
            return true;
        }

        return false;
    }

    public List<Subscriber> getTenAllTimeSubscribersWithBiggestBillsPaid(Integer clientId) {
        return subscriberRepository.getTenAllTimeSubscribersWithBiggestBillsPaid(clientId);
    }

    @Override
    public Map<Subscriber, Double> getSubscriberWithBiggestAmountPaid(Integer clientId, String fromDate, String toDate) {
        Object[] result = subscriberRepository.getSubscriberWithBiggestAmountPaid(clientId, fromDate, toDate);

        Map<Subscriber, Double> subscribers = new HashMap<>();
        for (Object object : result) {
            Object[] keyValueSet = (Object[]) object;
            subscribers.put((Subscriber) keyValueSet[0], (Double) keyValueSet[1]);
        }

        return subscribers;
    }

    @Override
    public Double getSubscriberAverageSumOfPaidInvoices(Integer subscriberId, String fromDate, String toDate) {
        double amount = subscriberRepository.getSubscriberAverageInvoiceSumPaid(subscriberId, fromDate, toDate);

        if (amount != 0) {
            amount = Math.round(amount * 100) / 100;
        }

        return amount;
    }

    @Override
    public List<Subscriber> getSubscribersByService(Integer serviceId)  {
        return subscriberRepository.getSubscribersByService(serviceId);
    }
}