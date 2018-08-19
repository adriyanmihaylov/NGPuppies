package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Address;

public interface AddressService {
    boolean create(Address admin);

    boolean update(Address admin);

    boolean  delete(Address address);
}
