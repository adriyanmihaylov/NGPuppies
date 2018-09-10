package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.ClientDetail;

public interface ClientDetailRepository {
    boolean create(ClientDetail clientDetail);

    boolean update(ClientDetail clientDetail);

    ClientDetail getById(Integer id);

    boolean addDetailsToClient(String clientUsername, ClientDetail clientDetail);
}
