package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.PasswordResetToken;
import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.repositories.base.AuthorityRepository;
import com.paymentsystem.ngpuppies.repositories.base.PasswordResetTokenRepository;
import com.paymentsystem.ngpuppies.web.dto.AdminDto;
import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository,
                            AuthorityRepository authorityRepository,
                            PasswordEncoder passwordEncoder,
                            PasswordResetTokenRepository passwordResetTokenRepository) {
        this.adminRepository = adminRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

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
            throw new Exception("Couldn't addIpAddress admin after first login! Authority not found!");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setAuthority(authority);
        admin.setLastPasswordResetDate(new Date());
        admin.setEnabled(Boolean.TRUE);

        return adminRepository.update(admin);
    }

    @Override
    public boolean createPasswordResetTokenForUser(Admin admin, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(admin, token);
        return passwordResetTokenRepository.create(passwordResetToken);
    }

    public String validatePasswordResetToken(long id, String token) {
        PasswordResetToken passToken =
                passwordResetTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser()
                .getId() != id)) {
            return "invalidToken";
        }

        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0) {
            return "expired";
        }

        User user = passToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                user, null, Arrays.asList(
                new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
    }
}