package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Client;

public interface ClientRepository {
    Client getByEik(String eik);

    boolean checkIfEikIsPresent(String eik);
}