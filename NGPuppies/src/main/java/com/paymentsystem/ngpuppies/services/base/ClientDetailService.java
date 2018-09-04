package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.ClientDetail;

public interface ClientDetailService {
    boolean create(ClientDetail clientDetail);

    boolean update(ClientDetail clientDetail);

    ClientDetail getById(Integer id);
}