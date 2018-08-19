package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Client;

import java.util.List;

public interface ClientRepository {
    List<Client> getAll();

    Client getByUsername(String username);

    Client getByEik(String eik);

    boolean create(Client client);

    boolean update(Client client);

    boolean deleteByUsername(String username);
}

