package com.paymentsystem.ngpuppies.repositories.base;

import java.util.List;

public interface GenericRepository<T> {

    public List<T> getAll();
}
