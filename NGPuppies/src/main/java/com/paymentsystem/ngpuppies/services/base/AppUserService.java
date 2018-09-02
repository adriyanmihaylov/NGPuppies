package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.users.AppUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AppUserService extends UserDetailsService {

    AppUser loadById(Integer id);

    List<AppUser> getAll();

    boolean deleteByUsername(String username);
}