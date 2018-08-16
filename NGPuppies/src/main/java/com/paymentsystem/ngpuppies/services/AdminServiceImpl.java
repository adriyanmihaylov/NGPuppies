package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Admin> getAll() {
        return adminRepository.getAll();
    }

    @Override
    public Admin getByUsername(String username) {
        return adminRepository.getByUsername(username);
    }

    @Override
    public boolean create(Admin admin) {
        return adminRepository.create(admin);
    }

    @Override
    public boolean update(Admin admin) {
        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        return adminRepository.deleteByUsername(username);
    }

    @Override
    public boolean checkIfUsernameIsPresent(String username) {
        return adminRepository.getByUsername(username) == null;
    }

    @Override
    public boolean checkIfEmailIsPresent(String email) {
        return  adminRepository.getByEmail(email) == null;
    }
}