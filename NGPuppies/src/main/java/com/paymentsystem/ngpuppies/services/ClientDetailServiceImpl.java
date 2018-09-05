package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.repositories.base.ClientDetailRepository;
import com.paymentsystem.ngpuppies.services.base.ClientDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientDetailServiceImpl implements ClientDetailService {

    @Autowired
    private ClientDetailRepository clientDetailRepository;

    @Override
    public boolean create(ClientDetail clientDetail) {
        return clientDetailRepository.create(clientDetail);
    }

    @Override
    public boolean update(ClientDetail clientDetail) {
        return clientDetailRepository.update(clientDetail);
    }

    @Override
    public ClientDetail getById(Integer id) {
        return clientDetailRepository.getById(id);
    }

}
