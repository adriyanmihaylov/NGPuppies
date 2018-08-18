package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.repositories.base.GenericUserRepository;
import com.paymentsystem.ngpuppies.services.base.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements GenericUserService<Client> {
    @Autowired
    private GenericUserRepository<Client> clientRepository;


    @Override
    public List<Client> getAll() {
        return clientRepository.getAll();
    }

    @Override
    public Client getByUsername(String username) {
        return clientRepository.getByUsername(username);
    }
}