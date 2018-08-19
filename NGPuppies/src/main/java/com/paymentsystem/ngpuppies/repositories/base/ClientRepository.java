package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Client;

import java.util.List;

public interface ClientRepository {
    List<Client> getAll();
    Client getByUsername(String username);
    boolean deleteByUsername(String username);
    boolean update(Client updateClient);
    boolean create(Client clientToBeCreated);
}
