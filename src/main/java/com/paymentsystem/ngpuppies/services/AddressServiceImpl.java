package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.repositories.base.AddressRepository;
import com.paymentsystem.ngpuppies.services.base.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> getAll() {
        return addressRepository.getAll();
    }

    @Override
    public Address getById(int id) {
        return addressRepository.getById(id);
    }

    @Override
    public boolean create(Address address) {
        return addressRepository.create(address);
    }

    @Override
    public boolean update(Address address) {
        return addressRepository.update(address);
    }

    @Override
    public boolean delete(Address address) {
        return false;
    }
}