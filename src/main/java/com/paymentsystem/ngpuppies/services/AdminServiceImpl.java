package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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
    public Admin loadByEmail(String email) {
        return adminRepository.loadByEmail(email);
    }

    @Override
    public Admin loadByUsername(String username) {
        return adminRepository.loadByUsername(username);
    }

    @Override
    public boolean create(Admin admin) throws SQLException {
        return adminRepository.create(admin);
    }

    @Override
    public boolean update(Admin admin) throws SQLException {
        return adminRepository.update(admin);
    }
}