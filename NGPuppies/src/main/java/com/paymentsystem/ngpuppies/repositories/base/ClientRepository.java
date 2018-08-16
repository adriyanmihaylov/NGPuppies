package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Client;

import java.util.List;

public interface ClientRepository {
    List<Client> getAll();
}
