package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Address;

public interface AddressRepository {
    boolean create(Address address);
    boolean delete(int id);
}
