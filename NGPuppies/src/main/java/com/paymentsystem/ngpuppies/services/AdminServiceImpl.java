package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.repositories.AdminRepositoryImpl;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import com.paymentsystem.ngpuppies.services.base.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements GenericUserService<Admin>,AdminService {

    @Autowired
    private AdminRepositoryImpl adminRepository;

    @Override
    public List<Admin> getAll() {
        return adminRepository.getAll();
    }

    @Override
    public Admin getByUsername(String username) {
        return adminRepository.getByUsername(username);
    }

    @Override
    public boolean create(Admin model) {
       return adminRepository.create(model);
    }

    @Override
    public boolean checkIfEmailIsPresent(String email) {
        return adminRepository.checkIfEmailIsPresent(email);
    }
}
