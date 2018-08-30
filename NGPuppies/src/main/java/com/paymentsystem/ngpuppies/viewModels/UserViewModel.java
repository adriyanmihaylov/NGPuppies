package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.users.ApplicationUser;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;

public class UserViewModel {
    public int id;

    public String username;

    public AuthorityName authority;


    public static UserViewModel fromModel(ApplicationUser user) {
        UserViewModel userViewModel = new UserViewModel();
        if (user != null) {
            userViewModel.id = user.getId();
            userViewModel.username = user.getUsername();
            userViewModel.authority = user.getAuthority().getName();
        }

        return userViewModel;
    }
}