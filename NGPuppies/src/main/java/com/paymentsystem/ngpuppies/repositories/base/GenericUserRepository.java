package com.paymentsystem.ngpuppies.repositories.base;

import java.util.List;

public interface GenericUserRepository<T> {
    List<T> getAll();

    T getByUsername(String username);
}