package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.users.Client;

import java.util.List;

public interface ClientRepository {
    List<Client> getAll();

    Client getByEik(String eik);

    Client loadByUsername(String username);

    boolean create(Client client);
}