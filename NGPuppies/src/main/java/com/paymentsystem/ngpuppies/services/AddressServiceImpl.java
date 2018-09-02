package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.repositories.base.AddressRepository;
import com.paymentsystem.ngpuppies.services.base.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address create(Address address) {
        return addressRepository.create(address);
    }

    @Override
    public boolean deleteById(int id) {
        return addressRepository.delete(id);
    }

    @Override
    public boolean update(Address address) {
        return addressRepository.update(address);
    }
}