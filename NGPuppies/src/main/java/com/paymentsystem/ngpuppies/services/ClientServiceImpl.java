package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.repositories.ClientRepositoryImpl;
import com.paymentsystem.ngpuppies.repositories.base.GenericUserRepository;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.services.base.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements GenericUserService<Client>,ClientService {
    @Autowired
    private ClientRepositoryImpl clientRepository;


    @Override
    public List<Client> getAll() {
        return clientRepository.getAll();
    }

    @Override
    public Client getByUsername(String username) {
        return clientRepository.getByUsername(username);
    }

    @Override
    public boolean create(Client model) {
        return clientRepository.create(model);
    }

    @Override
    public boolean checkIfEikIsPresent(String eik) {
        return clientRepository.checkIfEikIsPresent(eik);
    }
}