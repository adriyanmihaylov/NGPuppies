package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.users.Admin;

import java.sql.SQLException;
import java.util.List;

public interface AdminService {
    List<Admin> getAll();

    Admin loadByEmail(String email);

    Admin loadByUsername(String username);

    boolean create(Admin admin) throws SQLException;

    boolean update(Admin admin) throws SQLException;
}