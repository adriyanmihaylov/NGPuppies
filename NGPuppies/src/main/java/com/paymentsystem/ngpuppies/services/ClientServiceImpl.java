package com.paymentsystem.ngpuppies.services;


import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAll() {
        return null;
    }

    @Override
    public Client getByUsername(String username) {
        return null;
    }

    @Override
    public boolean create(Client client) {
        return false;
    }

    @Override
    public boolean update(Client client) {
        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        return false;
    }

    @Override
    public boolean checkIfUsernameIsPresent(String username) {
        return false;
    }

    @Override
    public boolean checkIfEikIsPresent(String eik) {
        return false;
    }
}
