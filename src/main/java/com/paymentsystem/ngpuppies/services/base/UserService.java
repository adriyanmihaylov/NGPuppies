package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.IpAddress;
import com.paymentsystem.ngpuppies.models.users.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User loadById(Integer id);

    UserDetails loadUserByUsername(String username);

    List<User> getAll();

    boolean deleteByUsername(String username);

    boolean addIpAddress(User user, IpAddress ipAddress);
}
