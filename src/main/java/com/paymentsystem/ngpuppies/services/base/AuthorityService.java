package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;

public interface AuthorityService {
    Authority getById(int id);

    Authority getByName(AuthorityName name);
}
