package com.paymentsystem.ngpuppies.services;


import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Client> getAll() {
        return clientRepository.getAll();
    }

    @Override
    public Client getByUsername(String username) {
        return clientRepository.getByUsername(username);
    }

    @Override
    public Client getByEik(String eik) {
        return clientRepository.getByEik(eik);
    }

    @Override
    public boolean create(Client client) {
        return clientRepository.create(client);
    }

    @Override
    public boolean update(Client client) {
        return clientRepository.update(client);
    }

    @Override
    public boolean deleteByUsername(String username) {
        return clientRepository.deleteByUsername(username);
    }

    @Override
    public boolean checkIfUsernameIsPresent(String username) {
        return this.getByUsername(username) == null && adminRepository.getByUsername(username) == null;
    }

    @Override
    public boolean checkIfEikIsPresent(String eik) {
        return this.getByEik(eik) == null;
    }
}
