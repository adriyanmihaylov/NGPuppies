package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.web.dto.AdminDTO;
import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import com.paymentsystem.ngpuppies.services.base.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Admin> getAll() {
        return adminRepository.getAll();
    }

    @Override
    public Admin loadByEmail(String email) {
        return adminRepository.loadByEmail(email);
    }

    @Override
    public Admin loadByUsername(String username) {
        return adminRepository.loadByUsername(username);
    }

    @Override
    public boolean create(AdminDTO adminDTO) throws InvalidParameterException, SQLException {
        if (adminDTO.getPassword() == null) {
            throw new InvalidParameterException("Password is missing!");
        }

        Authority authority = authorityService.getByName(AuthorityName.ROLE_INITIAL);
        Admin admin = new Admin(adminDTO.getUsername(), passwordEncoder.encode(adminDTO.getPassword()), adminDTO.getEmail(), authority);
        admin.setEnabled(Boolean.FALSE);

        return adminRepository.create(admin);
    }


    @Override
    public boolean update(String username, AdminDTO adminDTO) throws InvalidParameterException, SQLException {
        Admin admin = adminRepository.loadByUsername(username);
        if (admin == null) {
            throw new InvalidParameterException("Username not found!");
        }

        admin.setUsername(adminDTO.getUsername());
        admin.setEmail(adminDTO.getEmail());

        if (adminDTO.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
            admin.setLastPasswordResetDate(new Date());
        }

        return adminRepository.update(admin);
    }

    @Override
    public boolean updateOnFirstLogin(Admin admin) throws SQLException {
        Authority authority = authorityService.getByName(AuthorityName.ROLE_ADMIN);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setAuthority(authority);
        admin.setLastPasswordResetDate(new Date());
        admin.setEnabled(Boolean.TRUE);

        return adminRepository.update(admin);
    }
}