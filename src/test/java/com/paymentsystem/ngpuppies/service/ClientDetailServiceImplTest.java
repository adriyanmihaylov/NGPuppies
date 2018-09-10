package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.repositories.base.ClientDetailRepository;
import com.paymentsystem.ngpuppies.services.ClientDetailServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientDetailServiceImplTest {

    @Mock
    private ClientDetailRepository clientDetailRepository;

    @InjectMocks
    private ClientDetailServiceImpl clientDetailService;

    @Test
    public void create_whenClientDetailIsNull_shouldReturnFalse() {
        Assert.assertFalse(clientDetailService.create(null));
    }

    @Test
    public void create_onSuccess_shouldReturnTrue() {
        when(clientDetailRepository.create(any(ClientDetail.class))).thenReturn(true);

        Assert.assertTrue(clientDetailService.create(new ClientDetail()));
    }
}
