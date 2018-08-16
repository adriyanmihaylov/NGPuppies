package com.paymentsystem.ngpuppies.services.base;


import com.paymentsystem.ngpuppies.models.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAll();

    Client getByUsername(String username);

    Client getByEik(String eik);

    boolean create(Client client);

    boolean update(Client client);

    boolean deleteByUsername(String username);

    boolean checkIfUsernameIsPresent(String username);

    boolean checkIfEikIsPresent(String eik);
}