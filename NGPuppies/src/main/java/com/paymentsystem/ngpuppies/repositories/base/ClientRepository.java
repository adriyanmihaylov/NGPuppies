package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Client;

import java.util.List;

public interface ClientRepository {
    List<Client> getAll();
<<<<<<< HEAD
    Client getByUsername(String username);
    boolean deleteByUsername(String username);
    boolean update(Client updateClient);
    boolean create(Client clientToBeCreated);
}
=======

    Client getByUsername(String username);

    Client getByEik(String eik);

    boolean create(Client client);

    boolean update(Client client);

    boolean deleteByUsername(String username);
}
>>>>>>> 6f0af6ff06fddf3283d7d4b37bdd7ee1d7aa1594
