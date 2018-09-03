package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.ClientDetail;

public interface ClientDetailRepository {
    boolean create(ClientDetail clientDetail);

    boolean update(ClientDetail clientDetail);

    boolean delete(ClientDetail clientDetail);
}
