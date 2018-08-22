package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.users.Admin;

public interface AdminRepository {
    Admin getByEmail(String email);

    boolean checkIfEmailIsPresent(String email);
}
