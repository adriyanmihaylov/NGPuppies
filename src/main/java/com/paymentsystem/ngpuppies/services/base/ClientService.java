package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.web.dto.ClientDto;
import com.paymentsystem.ngpuppies.models.users.Client;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

public interface ClientService {
    List<Client> getAll();

    Client loadByUsername(String username);

    boolean create(ClientDto clientDto) throws Exception;

    boolean update(String username, ClientDto clientDto) throws Exception;

}