package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Address;

public interface AddressService {
    boolean create(Address address);
    boolean deleteById(int id);
}
