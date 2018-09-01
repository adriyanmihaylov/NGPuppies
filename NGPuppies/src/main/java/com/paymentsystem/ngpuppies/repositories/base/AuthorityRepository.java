package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;

public interface AuthorityRepository {
    Authority getById(int id);

    Authority getByName(AuthorityName name);
}
