package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Subscriber getByNumber(String phoneNumber) {
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
    public Map<Subscriber, Double> getTopTenSubscribers(Integer clientId) {
        Object[] result = subscriberRepository.getTopTenSubscribers(clientId);

        Map<Subscriber, Double> subscribers = new HashMap<>();
        for (Object object : result) {
            Object[] keyValueSet = (Object[]) object;
            subscribers.put((Subscriber) keyValueSet[0], (Double) keyValueSet[1]);
        }

        return subscribers;
    }

    @Override
    public Double getSubscriberAverageSumOfPaidInvoices(Integer subscriberId, String fromDate, String toDate) {
        return subscriberRepository.getSubscriberAverageInvoiceSumPaid(subscriberId,fromDate,toDate);
    }
}
