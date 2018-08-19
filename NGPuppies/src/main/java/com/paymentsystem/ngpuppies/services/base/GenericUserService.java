package com.paymentsystem.ngpuppies.services.base;

import java.util.List;

public interface GenericUserService<T> {
    List<T> getAll();

    T getByUsername(String username);

    boolean create(T model);
}