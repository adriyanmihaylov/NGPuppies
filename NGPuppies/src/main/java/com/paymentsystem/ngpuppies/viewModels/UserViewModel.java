package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.users.ApplicationUser;

public class UserViewModel {
    public int id;

    public String username;


    public static UserViewModel fromModel(ApplicationUser user) {
        UserViewModel userViewModel = new UserViewModel();
        if (user != null) {
            userViewModel.id = user.getId();
            userViewModel.username = user.getUsername();
        }

        return userViewModel;
    }
}