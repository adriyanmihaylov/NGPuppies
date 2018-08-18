package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.ApplicationUser;

import java.util.List;

public interface ApplicationUserRepository {
    List<ApplicationUser> getAll();
}
