package com.paymentsystem.ngpuppies.services.base;

public interface ApplicationUserService {
    boolean checkIfUsernameIsPresent(String username);

    boolean deleteByUsername(String username);
}
