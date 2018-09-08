package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.web.dto.AdminDTO;
import com.paymentsystem.ngpuppies.models.users.Admin;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

public interface AdminService {
    List<Admin> getAll();

    Admin loadByEmail(String email);

    Admin loadByUsername(String username);

    boolean create(AdminDTO adminDTO) throws InvalidParameterException, SQLException;

    boolean update(String username,AdminDTO adminDTO) throws InvalidParameterException,SQLException;

    boolean updateOnFirstLogin(Admin admin) throws SQLException;
}