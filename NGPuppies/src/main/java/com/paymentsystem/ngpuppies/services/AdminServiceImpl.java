package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.repositories.base.GenericRepository;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private GenericRepository<Admin> adminRepository;

    @Override
    public List<Admin> getAll() {
        return adminRepository.getAll();
    }
}
