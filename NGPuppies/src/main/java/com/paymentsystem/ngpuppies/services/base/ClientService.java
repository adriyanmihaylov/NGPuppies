package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.users.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAll();

    Client loadByEik(String eik);

    Client loadByUsername(String username);

    boolean create(Client client) throws Exception;

    boolean update(Client client) throws Exception;
}