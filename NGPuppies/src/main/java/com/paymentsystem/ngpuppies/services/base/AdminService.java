package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAll();

    Admin getByUsername(String username);

    boolean create(Admin admin);

    boolean update(Admin admin);

    boolean deleteByUsername(String username);

    boolean checkIfUsernameIsPresent(String username);

    boolean checkIfEmailIsPresent(String email);
}
