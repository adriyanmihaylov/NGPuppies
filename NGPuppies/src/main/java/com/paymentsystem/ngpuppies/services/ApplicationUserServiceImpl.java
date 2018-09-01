package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.users.ApplicationUser;
import com.paymentsystem.ngpuppies.repositories.ApplicationUserRepositoryImpl;
import com.paymentsystem.ngpuppies.services.base.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    @Autowired
    private ApplicationUserRepositoryImpl applicationUserRepository;

    @Override
    public List<ApplicationUser> getAll() {
        return applicationUserRepository.getAll();
    }

    @Override
    public ApplicationUser getByUsername(String username) {
        return applicationUserRepository.getByUsername(username);
    }

    @Override
    public boolean checkIfUsernameIsPresent(String username) {
        return applicationUserRepository.checkIfUsernameIsPresent(username);
    }

    @Override
    public boolean deleteByUsername(String username) {
        return applicationUserRepository.deleteByUsername(username);
    }

}