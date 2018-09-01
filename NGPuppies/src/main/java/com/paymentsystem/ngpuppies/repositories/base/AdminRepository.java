package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.exceptions.EmailIsPresentException;
import com.paymentsystem.ngpuppies.exceptions.UsernameIsPresentException;
import com.paymentsystem.ngpuppies.models.users.Admin;

import java.util.List;

public interface AdminRepository {
    List<Admin> getAll();

    Admin getByEmail(String email);

    Admin loadByUsername(String username);

    boolean create(Admin admin) throws Exception;
}