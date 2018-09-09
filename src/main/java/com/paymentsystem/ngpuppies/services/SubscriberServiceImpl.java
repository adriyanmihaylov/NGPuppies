package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import com.paymentsystem.ngpuppies.web.dto.SubscriberDto;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.repositories.base.TelecomServRepository;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.AlreadyBoundException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final TelecomServRepository telecomServRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public SubscriberServiceImpl(SubscriberRepository subscriberRepository, TelecomServRepository telecomServRepository, ClientRepository clientRepository) {
        this.subscriberRepository = subscriberRepository;
        this.telecomServRepository = telecomServRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Subscriber> getAll() {
        return subscriberRepository.getAll();
    }

    @Override
    public Subscriber getSubscriberByPhone(String phoneNumber) {
        return subscriberRepository.getSubscriberByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean create(SubscriberDto subscriberDto) throws InvalidParameterException, SQLException {
        if(subscriberDto.getAddress() == null) {
            subscriberDto.setAddress(new Address());
        }
        Subscriber subscriber = new Subscriber(subscriberDto.getFirstName(),
                subscriberDto.getLastName(),
                subscriberDto.getPhone(),
                subscriberDto.getEgn(),
                subscriberDto.getAddress(),
                0D);

        if (subscriberDto.getClient() != null) {
            Client client = clientRepository.loadByUsername(subscriberDto.getClient());
            if (client == null) {
                throw new InvalidParameterException("Client not found");
            }

            subscriber.setClient(client);
        }

        return subscriberRepository.create(subscriber);
    }

    @Override
    public boolean update(String phoneNumber, SubscriberDto subscriberDto) throws InvalidParameterException, SQLException {
        Subscriber subscriber = subscriberRepository.getSubscriberByPhoneNumber(phoneNumber);
        if (subscriber == null) {
            throw new InvalidParameterException("Subscriber not found!");
        }
        subscriber.setPhone(subscriberDto.getPhone());
        subscriber.setFirstName(subscriberDto.getFirstName());
        subscriber.setLastName(subscriberDto.getLastName());

        Client client = null;
        if (subscriberDto.getClient() != null) {
            client = clientRepository.loadByUsername(subscriberDto.getClient());
            if (client == null) {
                throw new InvalidParameterException("There is no such client!");
            }
        }

        subscriber.setClient(client);

        if (subscriberDto.getAddress() != null) {
            int id = subscriber.getAddress().getId();
            subscriber.setAddress(subscriberDto.getAddress());
            subscriber.getAddress().setId(id);
        }

        return subscriberRepository.update(subscriber);
    }

    @Override
    public boolean deleteSubscriberByNumber(String phoneNumber) {
        Subscriber subscriber = subscriberRepository.getSubscriberByPhoneNumber(phoneNumber);

        if (subscriber == null) {
            throw new IllegalArgumentException("There is no such subscriber in the database!");
        }

        return subscriberRepository.delete(subscriber);
    }

    @Override
    public boolean addServiceToSubscriber(String subscriberPhone, String serviceName) throws InvalidParameterException, AlreadyBoundException, SQLException {
        Subscriber subscriber = subscriberRepository.getSubscriberByPhoneNumber(subscriberPhone);
        if (subscriber == null) {
            throw new InvalidParameterException("Subscriber not found!");
        }
        TelecomServ currentService = telecomServRepository.getByName(serviceName);
        if (currentService == null) {
            throw new InvalidParameterException("There is no such service!");
        }

        if (subscriber.getSubscriberServices() == null) {
            subscriber.setSubscriberServices(new HashSet<>());
        } else if(subscriber.getSubscriberServices().contains(currentService)) {
            throw new AlreadyBoundException("The subscriber is already using this service");
        }

        subscriber.getSubscriberServices().add(currentService);

        return subscriberRepository.update(subscriber);
    }

    public List<Subscriber> getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(int clientId) {
        return subscriberRepository.getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(clientId);
    }

    @Override
    public Map<Subscriber, Double> getSubscriberOfClientWithBiggestAmountPaid(int subscriberId, String fromDate, String toDate) throws InvalidParameterException {
        validateDate(fromDate,toDate);
        Object[] result = subscriberRepository.getSubscriberOfClientWithBiggestAmountPaid(subscriberId, fromDate, toDate);

        Map<Subscriber, Double> subscribers = new HashMap<>();
        for (Object object : result) {
            Object[] keyValueSet = (Object[]) object;
            subscribers.put((Subscriber) keyValueSet[0], (Double) keyValueSet[1]);
        }

        return subscribers;
    }

    @Override
    public Double getSubscriberAverageSumOfPaidInvoices(Subscriber subscriber, String fromDate, String toDate) throws InvalidParameterException{
        validateDate(fromDate,toDate);

        if(subscriber == null) {
            throw new InvalidParameterException("There is no such subscriber!");
        }

        return subscriberRepository.getSubscriberAverageInvoiceSumPaid(subscriber.getId(), fromDate, toDate);
    }

    @Override
    public List<Subscriber> getAllSubscribersUsingServiceByServiceName(String serviceName) throws InvalidParameterException {
        TelecomServ telecomServ = telecomServRepository.getByName(serviceName);
        if (telecomServ == null) {
            throw new InvalidParameterException("There is no such service!");
        }

        return subscriberRepository.getAllSubscribersByService(telecomServ.getId());
    }

    public boolean validateDate(String start, String end) throws InvalidParameterException{
        try {
            if (start.equals(end)) {
                return true;
            }

            if (LocalDate.parse(start).isAfter(LocalDate.parse(end))) {
                throw new InvalidParameterException("Invalid date range!");
            }
        } catch (Exception e) {
            throw new InvalidParameterException("Invalid dates!");
        }

        return true;
    }
}