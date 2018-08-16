package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Admin;

import java.util.List;

public interface AdminRepository {

    List<Admin> getAll();

    Admin getByUsername(String username);

    Admin getByEmail(String email);


    boolean create(Admin admin);

    boolean deleteByUsername(String username);
}