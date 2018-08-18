package com.paymentsystem.ngpuppies.repositories.base;

public interface ApplicationUserRepository {
    boolean checkIfUsernameIsPresent(String username);
}
