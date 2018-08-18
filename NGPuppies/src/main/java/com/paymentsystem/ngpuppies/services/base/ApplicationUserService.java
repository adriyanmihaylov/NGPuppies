package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.ApplicationUser;

import java.util.List;

public interface ApplicationUserService {
    boolean checkIfUsernameIsPresent(String username);

    boolean deleteByUsername(String username);
}
