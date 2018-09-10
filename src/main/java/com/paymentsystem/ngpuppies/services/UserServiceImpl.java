package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.repositories.base.UserRepository;
import com.paymentsystem.ngpuppies.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loadById(Integer id) {
        return userRepository.loadById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public boolean deleteByUsername(String username) throws InvalidParameterException {
        if (username == null) {
            throw new InvalidParameterException("Username not found!");
        }

        return userRepository.delete(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.loadByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        return user;
    }
}