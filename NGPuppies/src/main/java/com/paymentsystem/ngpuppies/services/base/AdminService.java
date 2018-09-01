package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.exceptions.EmailIsPresentException;
import com.paymentsystem.ngpuppies.exceptions.UsernameIsPresentException;
import com.paymentsystem.ngpuppies.models.users.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAll();

    Admin getByUsername(String username);

    boolean create(Admin admin) throws Exception;
}