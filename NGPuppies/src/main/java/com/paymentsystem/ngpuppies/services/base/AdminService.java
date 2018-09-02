package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.users.Admin;

import java.util.List;

public interface AdminService {
    List<Admin> getAll();

    Admin loadByUsername(String username);

    boolean create(Admin admin) throws Exception;

    boolean update(Admin admin) throws Exception;
}