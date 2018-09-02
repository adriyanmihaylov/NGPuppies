package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.ClientDetail;

public interface ClientDetailRepository {
    ClientDetail create(ClientDetail clientDetail);

    boolean update(ClientDetail clientDetail);
}
