package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.users.AppUser;

import java.util.List;

public interface AppUserService {
    List<AppUser> getAll();

    AppUser loadByUsername(String username);

    boolean deleteByUsername(String username);
}