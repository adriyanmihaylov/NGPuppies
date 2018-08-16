package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.services.base.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService {
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