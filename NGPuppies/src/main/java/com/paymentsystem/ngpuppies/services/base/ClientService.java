package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.users.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAll();

    Client getByUsername(String username);

    boolean create(Client client);
}