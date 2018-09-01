package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.users.AppUser;
import com.paymentsystem.ngpuppies.repositories.base.AppUserRepository;
import com.paymentsystem.ngpuppies.services.base.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public List<AppUser> getAll() {
        return appUserRepository.getAll();
    }

    @Override
    public AppUser loadByUsername(String username) {
        return appUserRepository.loadByUsername(username);
    }


    @Override
    public boolean deleteByUsername(String username) {
        return appUserRepository.deleteByUsername(username);
    }

}