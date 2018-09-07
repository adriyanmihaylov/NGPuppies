package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.models.dto.ClientDTO;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.ClientDetailRepository;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientDetailRepository clientDetailRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Client> getAll() {
        return clientRepository.getAll();
    }

    @Override
    public Client loadByEik(String eik) {
        return clientRepository.loadByEik(eik);
    }

    @Override
    public Client loadByUsername(String username) {
        return clientRepository.loadByUsername(username);
    }

    @Override
    public boolean create(Client model) throws SQLException {
        return clientRepository.create(model);
    }

    @Override
    public boolean update(Client client, ClientDTO clientDTO) throws SQLException {
        client.setUsername(clientDTO.getUsername());
        client.setEik(clientDTO.getEik());


        if (clientDTO.getDetails() != null) {
            if (client.getDetails() != null) {
                int id = client.getDetails().getId();
                client.setDetails(clientDTO.getDetails());
                client.getDetails().setId(id);
            } else {
                client.setDetails(clientDTO.getDetails());
            }
        }

        if (clientDTO.getPassword() != null) {
            client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
            client.setLastPasswordResetDate(new Date());
        }

        return clientRepository.update(client);
    }
}