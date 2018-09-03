package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.users.Client;
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
        return clientRepository.getAll();
    }

    @Override
    public Client loadByEik(String eik) {
        return clientRepository.loadByEik(eik);
    }

    @Override
    public Client loadByUsername(String username) {
        return clientRepository.loadByUsername(username);
    }

    @Override
    public boolean create(Client model) throws Exception {
        return clientRepository.create(model);
    }

    @Override
    public boolean update(Client client) throws Exception {
        return clientRepository.update(client);
    }
}