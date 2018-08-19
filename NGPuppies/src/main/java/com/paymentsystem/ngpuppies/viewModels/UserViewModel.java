package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.ApplicationUser;

public class UserViewModel {
    public int id;

    public String username;

    public String role;

    public static UserViewModel fromModel(ApplicationUser user) {
        UserViewModel userViewModel = new UserViewModel();
        if (user != null) {
            userViewModel.id = user.getId();
            userViewModel.username = user.getUsername();
            userViewModel.role = user.getRole();
        }

        return userViewModel;
    }
}