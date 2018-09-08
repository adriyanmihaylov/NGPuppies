package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.models.dto.ClientDTO;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.ClientDetailRepository;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import com.paymentsystem.ngpuppies.services.base.AuthorityService;
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
    private AuthorityService authorityService;
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
    public boolean create(ClientDTO clientDTO) throws IllegalArgumentException,SQLException {
        if (clientDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password is missing");
        }

        Authority authority = authorityService.getByName(AuthorityName.ROLE_CLIENT);
        if (clientDTO.getDetails() == null) {
            clientDTO.setDetails(new ClientDetail());
        }

        Client client = new Client(clientDTO.getUsername(),
                clientDTO.getPassword(),
                clientDTO.getEik(),
                authority,
                clientDTO.getDetails());

        return clientRepository.create(client);
    }

    @Override
    public boolean update(String username, ClientDTO clientDTO) throws IllegalArgumentException, SQLException {
        Client client = clientRepository.loadByUsername(username);

        if (client == null) {
            throw new IllegalArgumentException("There is no such client!");
        }

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