package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public boolean deleteByNumber(String phoneNumber) {
        return subscriberRepository.deleteByNumber(phoneNumber);
    }

    @Override
    public boolean update(Subscriber updatedSubscriber) throws Exception {
        return subscriberRepository.update(updatedSubscriber);
    }

    @Override
    public boolean create(Subscriber subscriber) throws Exception {
        return subscriberRepository.create(subscriber);
    }
}
