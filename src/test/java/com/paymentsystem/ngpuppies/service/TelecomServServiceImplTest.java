package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.TelecomServRepository;
import com.paymentsystem.ngpuppies.services.TelecomServServiceImpl;
import com.paymentsystem.ngpuppies.web.dto.TelecomServDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TelecomServServiceImplTest {
    @Mock
    private TelecomServRepository telecomServRepository;

    @InjectMocks
    private TelecomServServiceImpl telecomServService;

    private TelecomServ telecomServ;
    private int TELECOM_SERVICE_ID = 1;
    private String TELECOM_SERVICE_NAME = "INTERNET";

    private List<TelecomServ> telecomServList;

    @Before
    public void beforeTest() {
        telecomServList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            telecomServList.add(new TelecomServ());
        }
        telecomServ = new TelecomServ(TELECOM_SERVICE_NAME);
        telecomServ.setId(TELECOM_SERVICE_ID);
    }

    @Test
    public void getAll_onSuccess_shouldReturnListOfTelecomServ() {
        when(telecomServRepository.getAll()).thenReturn(telecomServList);

        List<TelecomServ> resultList = telecomServService.getAll();

        Assert.assertEquals(telecomServList, resultList);
    }

    @Test(expected = InvalidParameterException.class)
    public void getByName_whenServiceNameIsNull_shouldTrowException() {
        telecomServService.getByName(null);
    }

    @Test
    public void getByName_onSuccess_shouldTelecomServObject() {
        when(telecomServRepository.getByName(telecomServ.getName())).thenReturn(telecomServ);

        TelecomServ result = telecomServService.getByName(telecomServ.getName());

        Assert.assertEquals(telecomServ, result);
    }

    @Test(expected = InvalidParameterException.class)
    public void create_whenTelecomServNameIsNull_shouldTrowException() throws SQLException {
        TelecomServDto telecomServDto = new TelecomServDto();
        telecomServService.create(telecomServDto);
    }

    @Test
    public void create_onSuccess_shouldReturnTrue() throws SQLException {
        TelecomServDto telecomServDto = new TelecomServDto(TELECOM_SERVICE_NAME);
        when(telecomServRepository.create(any(TelecomServ.class))).thenReturn(true);

        boolean result = telecomServService.create(telecomServDto);

        Assert.assertTrue(result);
    }

    @Test(expected = InvalidParameterException.class)
    public void update_whenServiceNameIsNull_shouldThrowException() throws SQLException {
        telecomServService.update(null,new TelecomServDto());
    }

    @Test
    public void update_onSuccess_shouldReturnTrue() throws SQLException {
        String oldName = "oldName";
        String newName = "newName";
        TelecomServDto telecomServDto = new TelecomServDto(newName);
        when(telecomServRepository.update(oldName, telecomServDto.getName().toUpperCase())).thenReturn(true);

        boolean result = telecomServService.update(oldName, telecomServDto);

        Assert.assertTrue(result);
    }
    @Test(expected = InvalidParameterException.class)
    public void delete_whenServiceNameIsNull_shouldThrowException() {
        telecomServService.delete(null);
    }

    @Test
    public void delete_onSuccess_shouldReturnTrue() {
        when(telecomServRepository.delete(TELECOM_SERVICE_NAME)).thenReturn(true);

        boolean result = telecomServService.delete(TELECOM_SERVICE_NAME);

        Assert.assertTrue(result);
    }

    @Test
    public void getAllServicesOfClient_onSuccess_shouldReturnListOfTelecomServ() {
        Client client = new Client();
        client.setId(1);
        when(telecomServRepository.getAllServicesOfClientByClientId(client.getId())).thenReturn(telecomServList);

        List<TelecomServ> result = telecomServService.getAllServicesOfClient(client);

        Assert.assertEquals(telecomServList, result);
    }

}
