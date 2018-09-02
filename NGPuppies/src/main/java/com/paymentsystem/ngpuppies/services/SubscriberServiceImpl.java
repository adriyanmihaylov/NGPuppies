package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.SubscribersRepository;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubscriberServiceImpl implements SubscriberService {
    @Autowired
    private SubscribersRepository subscribersRepository;

    @Override
    public List<Subscriber> getAll() {
        return subscribersRepository.getAll();
    }

    @Override
    public Subscriber getByNumber(String phoneNumber) {
        return subscribersRepository.getByNumber(phoneNumber);
    }

    @Override
    public boolean deleteByNumber(String phoneNumber) {
        return subscribersRepository.deleteByNumber(phoneNumber);
    }

    @Override
    public boolean update(Subscriber updatedSubscriber) {
        return subscribersRepository.update(updatedSubscriber);
    }

    @Override
    public boolean create(Subscriber subscriber) {
        return subscribersRepository.create(subscriber);
    }

    @Override
    public boolean checkIfPhoneExists(String phoneNumber) {
        Subscriber getByNumber = null;
        try {
            getByNumber = getByNumber(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (getByNumber == null) {
            return false;
        } else {
            return true;
        }
    }
}