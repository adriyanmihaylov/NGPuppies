package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.web.dto.AdminDto;
import com.paymentsystem.ngpuppies.models.users.Admin;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.List;

public interface AdminService {
    List<Admin> getAll();

    Admin loadByEmail(String email);

    Admin loadByUsername(String username);

    boolean create(AdminDto adminDto) throws Exception;

    boolean update(String username,AdminDto adminDto) throws InvalidParameterException,SQLException;

    boolean updateOnFirstLogin(Admin admin) throws Exception;

    boolean createPasswordResetTokenForUser(Admin admin, String token);

}