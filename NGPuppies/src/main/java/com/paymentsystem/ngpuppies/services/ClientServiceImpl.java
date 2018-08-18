package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.repositories.base.GenericRepository;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private GenericRepository<Client> clientRepository;


    @Override
    public List<Client> getAll() {
        return clientRepository.getAll();
    }
}