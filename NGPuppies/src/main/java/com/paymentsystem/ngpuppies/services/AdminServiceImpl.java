package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.repositories.base.GenericUserRepository;
import com.paymentsystem.ngpuppies.services.base.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements GenericUserService<Admin> {

    @Autowired
    private GenericUserRepository<Admin> adminRepository;

    @Override
    public List<Admin> getAll() {
        return adminRepository.getAll();
    }

    @Override
    public Admin getByUsername(String username) {
        return adminRepository.getByUsername(username);
    }
}
