package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAll();

    Address getById(int id);

    boolean create(Address address);

    boolean update(Address address);

    boolean delete(Address address);
}
