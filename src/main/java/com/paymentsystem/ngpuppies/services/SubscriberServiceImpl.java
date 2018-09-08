package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.web.dto.SubscriberDTO;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.repositories.base.TelecomServRepository;
import com.paymentsystem.ngpuppies.services.base.ClientService;
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
    @Autowired
    private SubscriberRepository subscriberRepository;
    @Autowired
    private TelecomServRepository telecomServRepository;
    @Autowired
    private ClientService clientService;

    @Override
    public List<Subscriber> getAll() {
        return subscriberRepository.getAll();
    }

    @Override
    public Subscriber getSubscriberByPhone(String phoneNumber) {
        return subscriberRepository.getSubscriberByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean create(SubscriberDTO subscriberDTO) throws InvalidParameterException, SQLException {
        if(subscriberDTO.getAddress() == null) {
            subscriberDTO.setAddress(new Address());
        }
        Subscriber subscriber = new Subscriber(subscriberDTO.getFirstName(),
                subscriberDTO.getLastName(),
                subscriberDTO.getPhone(),
                subscriberDTO.getEgn(),
                subscriberDTO.getAddress(),
                0D);

        if (subscriberDTO.getClient() != null) {
            Client client = clientService.loadByUsername(subscriberDTO.getClient());
            if (client == null) {
                throw new InvalidParameterException("Client not found");
            }

            subscriber.setClient(client);
        }

        return subscriberRepository.create(subscriber);
    }

    @Override
    public boolean update(String phoneNumber, SubscriberDTO subscriberDTO) throws InvalidParameterException, SQLException {
        Subscriber subscriber = subscriberRepository.getSubscriberByPhoneNumber(phoneNumber);
        if (subscriber == null) {
            throw new InvalidParameterException("Subscriber not found!");
        }
        subscriber.setPhone(subscriberDTO.getPhone());
        subscriber.setFirstName(subscriberDTO.getFirstName());
        subscriber.setLastName(subscriberDTO.getLastName());

        Client client = null;
        if (subscriberDTO.getClient() != null) {
            client = clientService.loadByUsername(subscriberDTO.getClient());
            if (client == null) {
                throw new InvalidParameterException("There is no such client!");
            }
        }

        subscriber.setClient(client);

        if (subscriberDTO.getAddress() != null) {
            int id = subscriber.getAddress().getId();
            subscriber.setAddress(subscriberDTO.getAddress());
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
        return subscriberRepository.getTenAllTimeSubscribersWithBiggestBillsPaid(clientId);
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

    private boolean validateDate(String start, String end) throws InvalidParameterException{
        if (start.equals(end)) {
            return true;
        }

        if (LocalDate.parse(start).isAfter(LocalDate.parse(end))) {
            throw new InvalidParameterException("Invalid date range!");
        }

        return true;
    }
}