package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.users.Admin;

import java.sql.SQLException;
import java.util.List;

public interface AdminRepository {
    List<Admin> getAll();

    Admin loadByEmail(String email);

    Admin loadByUsername(String username);

    boolean create(Admin admin) throws Exception;

    boolean update(Admin admin) throws Exception;
}