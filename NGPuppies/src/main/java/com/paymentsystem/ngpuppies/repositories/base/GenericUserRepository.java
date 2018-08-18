package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Client;

import java.util.List;

public interface GenericUserRepository<T> {
    List<T> getAll();

    T getByUsername(String username);

    boolean create(T model);
}