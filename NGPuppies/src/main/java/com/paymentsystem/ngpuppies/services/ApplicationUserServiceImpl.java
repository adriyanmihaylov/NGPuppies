package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.ApplicationUser;
import com.paymentsystem.ngpuppies.repositories.base.GenericUserRepository;
import com.paymentsystem.ngpuppies.services.base.GenericUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserServiceImpl implements GenericUserService<ApplicationUser> {

    @Autowired
    private GenericUserRepository<ApplicationUser> applicationUserRepository;

    @Override
    public List<ApplicationUser> getAll() {
        return applicationUserRepository.getAll();
    }

    @Override
    public ApplicationUser getByUsername(String username) {
        return applicationUserRepository.getByUsername(username);
    }
}