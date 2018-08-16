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
        return null;
    }

    @Override
    public Admin getByUsername(String username) {
        return null;
    }

    @Override
    public boolean create(Admin admin) {
        return false;
    }

    @Override
    public boolean update(Admin admin) {
        return false;
    }

    @Override
    public boolean deleteByUsername(String username) {
        return false;
    }

    @Override
    public boolean checkIfUsernameIsPresent(String username) {
        return false;
    }

    @Override
    public boolean checkIfEmailIsPresent(String email) {
        return false;
    }
}