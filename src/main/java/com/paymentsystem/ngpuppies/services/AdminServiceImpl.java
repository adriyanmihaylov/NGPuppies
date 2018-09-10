package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.repositories.base.AuthorityRepository;
import com.paymentsystem.ngpuppies.web.dto.AdminDto;
import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import com.paymentsystem.ngpuppies.services.base.AdminService;
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
    private AuthorityRepository authorityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Admin> getAll() {
        return adminRepository.getAll();
    }

    @Override
    public Admin loadByEmail(String email) {
        if(email == null) {
            return null;
        }
        return adminRepository.loadByEmail(email);
    }

    @Override
    public Admin loadByUsername(String username) {
        if(username == null) {
            return null;
        }
        return adminRepository.loadByUsername(username);
    }

    @Override
    public boolean create(AdminDto adminDto) throws Exception {
        if (adminDto == null ||  adminDto.getPassword() == null) {
            throw new InvalidParameterException("Password is missing!");
        }

        Authority authority = authorityRepository.getByName(AuthorityName.ROLE_INITIAL);
        if(authority == null) {
            throw new Exception("Could't register admin! Authority not found!");
        }
        Admin admin = new Admin(adminDto.getUsername(), passwordEncoder.encode(adminDto.getPassword()), adminDto.getEmail(), authority);
        admin.setEnabled(Boolean.FALSE);

        return adminRepository.create(admin);
    }


    @Override
    public boolean update(String username, AdminDto adminDto) throws InvalidParameterException, SQLException {
        if(username == null || adminDto == null) {
            throw new InvalidParameterException("Username must not be empty!");
        }

        Admin admin = adminRepository.loadByUsername(username);
        if (admin == null) {
            throw new InvalidParameterException("Username not found!");
        }

        admin.setUsername(adminDto.getUsername());
        admin.setEmail(adminDto.getEmail());

        if (adminDto.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            admin.setLastPasswordResetDate(new Date());
        }

        return adminRepository.update(admin);
    }

    @Override
    public boolean updateOnFirstLogin(Admin admin) throws Exception {
        if(admin == null) {
            return false;
        }

        Authority authority = authorityRepository.getByName(AuthorityName.ROLE_ADMIN);
        if(authority == null) {
            throw new Exception("Couldn't update admin after first login! Authority not found!");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setAuthority(authority);
        admin.setLastPasswordResetDate(new Date());
        admin.setEnabled(Boolean.TRUE);

        return adminRepository.update(admin);
    }
}