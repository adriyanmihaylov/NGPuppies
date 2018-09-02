package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.users.AppUser;

import java.util.List;

public interface AppUserRepository {

    AppUser loadById(Integer id);

    List<AppUser> getAll();

    AppUser loadByUsername(String username);

    boolean deleteByUsername(String username);
}
