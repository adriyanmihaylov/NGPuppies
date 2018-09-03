package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.ClientDetail;

public interface ClientDetailService {
    ClientDetail create(ClientDetail clientDetail);

    boolean update(ClientDetail clientDetail);
}
