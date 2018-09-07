package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.dto.ClientDTO;
import com.paymentsystem.ngpuppies.models.users.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientService {
    List<Client> getAll();

    Client loadByEik(String eik);

    Client loadByUsername(String username);

    boolean create(Client client) throws SQLException;

    public boolean update(Client client, ClientDTO clientDTO) throws SQLException;
}