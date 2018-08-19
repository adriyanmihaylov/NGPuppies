package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.repositories.base.AddressRepository;
import com.paymentsystem.ngpuppies.services.base.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    private AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }

    @Override
    public boolean create(Address address) {
        return addressRepository.create(address);
    }


    @Override
    public boolean deleteById(int id) {
       return addressRepository.delete(id);
    }
}
