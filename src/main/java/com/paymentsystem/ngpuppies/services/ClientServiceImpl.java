package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.repositories.base.AuthorityRepository;
import com.paymentsystem.ngpuppies.web.dto.ClientDto;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Client> getAll() {
        return clientRepository.getAll();
    }

    @Override
    public Client loadByUsername(String username) {
        return clientRepository.loadByUsername(username);
    }

    @Override
    public boolean create(ClientDto clientDto) throws Exception {
        if (clientDto.getPassword() == null) {
            throw new InvalidParameterException("Password is missing");
        }

        Authority authority = authorityRepository.getByName(AuthorityName.ROLE_CLIENT);
        if(authority == null) {
            throw new Exception("Couldn't find client authority!");
        }

        if (clientDto.getDetails() == null) {
            clientDto.setDetails(new ClientDetail());
        }

        Client client = new Client(clientDto.getUsername(),
                clientDto.getPassword(),
                clientDto.getEik(),
                authority,
                clientDto.getDetails());

        return clientRepository.create(client);
    }

    @Override
    public boolean update(String username, ClientDto clientDto) throws Exception {
        Client client = clientRepository.loadByUsername(username);

        if (client == null) {
            throw new InvalidParameterException("There is no such client!");
        }

        client.setUsername(clientDto.getUsername());
        client.setEik(clientDto.getEik());


        if (clientDto.getDetails() != null) {
            if (client.getDetails() != null) {
                int id = client.getDetails().getId();
                client.setDetails(clientDto.getDetails());
                client.getDetails().setId(id);
            } else {
                client.setDetails(clientDto.getDetails());
            }
        } else if (client.getDetails() == null) {
            throw new Exception("Can't addIpAddress client! Client details were not found!Please create details and add them to client!");
        }

        if (clientDto.getPassword() != null) {
            client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
            client.setLastPasswordResetDate(new Date());
        }

        return clientRepository.update(client);
    }
}