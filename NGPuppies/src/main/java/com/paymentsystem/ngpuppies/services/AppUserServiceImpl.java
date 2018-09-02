package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.users.AppUser;
import com.paymentsystem.ngpuppies.repositories.base.AppUserRepository;
import com.paymentsystem.ngpuppies.services.base.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService, UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public AppUser loadById(Integer id) {
        return appUserRepository.loadById(id);
    }

    @Override
    public List<AppUser> getAll() {
        return appUserRepository.getAll();
    }

    @Override
    public boolean deleteByUsername(String username) {
        return appUserRepository.deleteByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.loadByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        return appUserRepository.loadByUsername(username);
    }
}