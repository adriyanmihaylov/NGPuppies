package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.ApplicationUser;

import java.util.List;

public interface ApplicationUserService {
    List<ApplicationUser> getAll();

    ApplicationUser getByUsername(String username);

    boolean checkIfUsernameIsPresent(String username);
}
