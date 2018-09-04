package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.users.User;

import java.util.List;

public interface UserRepository {

    User loadById(Integer id);

    User loadByUsername(String username);

    List<User> getAll();

    boolean delete(User user);
}
