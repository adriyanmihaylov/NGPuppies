package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.ApplicationUser;
import com.paymentsystem.ngpuppies.repositories.base.GenericRepository;
import com.paymentsystem.ngpuppies.services.base.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    @Autowired
    private GenericRepository<ApplicationUser> applicationUserRepository;

    @Override
    public List<ApplicationUser> getAll() {
        return applicationUserRepository.getAll();
    }
}
