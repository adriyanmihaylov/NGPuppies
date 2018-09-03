package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Address;

public interface AddressRepository {
    Address create(Address address);

    boolean delete(int id);

    boolean update(Address address);
}
