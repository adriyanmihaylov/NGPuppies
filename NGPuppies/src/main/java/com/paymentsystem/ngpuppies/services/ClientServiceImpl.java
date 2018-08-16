package com.paymentsystem.ngpuppies.services;


import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.services.base.ClientService;

import java.util.List;

public class ClientServiceImpl implements ClientService {
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
