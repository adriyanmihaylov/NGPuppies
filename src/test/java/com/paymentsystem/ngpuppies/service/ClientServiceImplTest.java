package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.AuthorityRepository;
import com.paymentsystem.ngpuppies.repositories.base.ClientDetailRepository;
import com.paymentsystem.ngpuppies.repositories.base.ClientRepository;
import com.paymentsystem.ngpuppies.services.ClientServiceImpl;
import com.paymentsystem.ngpuppies.web.dto.ClientDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientDetailRepository clientDetailRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientDto clientDto;
    private List<Client> clientList;
    private Authority authority;

    @Before
    public void beforeTest() {
        authority = new Authority(AuthorityName.ROLE_CLIENT);
        client = new Client("username", "password", "123123123", authority);
        clientDto = new ClientDto("username", "password", "123123123");
        clientList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            clientList.add(new Client());
        }

    }

    @Test
    public void getAll_onSuccess_shouldReturnListOfClient() {
        when(clientRepository.getAll()).thenReturn(clientList);

        List<Client> resultList = clientService.getAll();

        Assert.assertEquals(clientList.size(), resultList.size());
    }

    @Test
    public void loadByUsername_whenUsernameIsNotPresent_shouldReturnNull() {
        when(clientRepository.loadByUsername(client.getUsername())).thenReturn(client);

        Client result = clientService.loadByUsername(client.getUsername());

        Assert.assertEquals(client, result);

    }

    @Test(expected = InvalidParameterException.class)
    public void create_whenPasswordIsNull_shouldThrowException() throws Exception {
        clientDto.setPassword(null);
        clientService.create(clientDto);
    }

    @Test(expected = Exception.class)
    public void create_whenAuthorityIsNull_shouldThrowException() throws Exception {
        when(clientRepository.create(any(Client.class))).thenReturn(true);
        when(authorityRepository.getByName(AuthorityName.ROLE_CLIENT)).thenReturn(null);

        clientService.create(clientDto);
    }

    @Test
    public void create_whenClientDetailsAreNull_shouldCreateNewDetailsAndSaveClient() throws Exception {
        clientDto.setDetails(null);
        when(clientRepository.create(any(Client.class))).thenReturn(true);
        when(authorityRepository.getByName(AuthorityName.ROLE_CLIENT)).thenReturn(authority);
        boolean result = clientService.create(clientDto);

        Assert.assertTrue(result);
        Assert.assertNotNull(clientDto.getDetails());
    }

    @Test(expected = InvalidParameterException.class)
    public void update_whenClientNotExists_shouldThrowException() throws Exception {
        when(clientRepository.loadByUsername(any(String.class))).thenReturn(null);
        clientService.update(client.getUsername(),clientDto);
    }

    @Test(expected = Exception.class)
    public void update_whenClientAndClientDtoDetailsAreNull_shouldCreateNewDetails() throws Exception {
        client.setDetails(null);
        clientDto.setDetails(null);
        when(clientRepository.loadByUsername(any(String.class))).thenReturn(client);
        when(clientDetailRepository.create(any(ClientDetail.class))).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("password");

        clientService.update(client.getUsername(),clientDto);
    }
    @Test
    public void update_whenClientDtoDetailsAreNotNull_shouldUpdateClientDetails() throws Exception {
        client.setDetails(null);
        clientDto.setDetails(new ClientDetail("clientDto Details"));
        when(clientRepository.loadByUsername(any(String.class))).thenReturn(client);
        when(clientDetailRepository.create(any(ClientDetail.class))).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("password");
//        when(clientRepository.addIpAddress(any(Client.class))).thenReturn(true);

        clientService.update(client.getUsername(),clientDto);

        Assert.assertNotNull(client.getDetails());
    }

    @Test
    public void update_whenClientAndClientDtoDetailsAreNotNull_shouldUpdateClientDetails() throws Exception {
        String expectedDescription = "new details";
        client.setDetails(new ClientDetail("client old Details"));
        clientDto.setDetails(new ClientDetail(expectedDescription));

        when(clientRepository.loadByUsername(any(String.class))).thenReturn(client);
        when(clientDetailRepository.create(any(ClientDetail.class))).thenReturn(true);
        when(passwordEncoder.encode(any())).thenReturn("password");
//        when(clientRepository.addIpAddress(any(Client.class))).thenReturn(true);

        boolean result = clientService.update(client.getUsername(),clientDto);

        Assert.assertEquals(expectedDescription,client.getDetails().getDescription());
    }
}
//
//            if (clientDto.getDetails() != null) {
//            if (client.getDetails() != null) {
//                int id = client.getDetails().getId();
//                client.setDetails(clientDto.getDetails());
//                client.getDetails().setId(id);
//            } else {
//                client.setDetails(clientDto.getDetails());
//            }
//        } else if(client.getDetails() == null) {
//            ClientDetail clientDetail = new ClientDetail();
//            if (!clientDetailRepository.create(clientDetail)) {
//                throw new Exception("Can not create new client details!");
//            }
//            c
//
//        if (clientDto.getPassword() != null) {
//            client.setPassword(passwordEncoder.encode(clientDto.getPassword()));
//            client.setLastPasswordResetDate(new Date());
//        }
//
//        return clientRepository.addIpAddress(client);
//    }
