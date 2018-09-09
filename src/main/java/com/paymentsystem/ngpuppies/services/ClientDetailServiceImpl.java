package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.repositories.base.ClientDetailRepository;
import com.paymentsystem.ngpuppies.services.base.ClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailServiceImpl implements ClientDetailService {
    private final ClientDetailRepository clientDetailRepository;

    @Autowired
    public ClientDetailServiceImpl(ClientDetailRepository clientDetailRepository) {
        this.clientDetailRepository = clientDetailRepository;
    }

    @Override
    public boolean create(ClientDetail clientDetail) {
        if (clientDetail == null) {

            return false;
        }
        return clientDetailRepository.create(clientDetail);
    }
}
