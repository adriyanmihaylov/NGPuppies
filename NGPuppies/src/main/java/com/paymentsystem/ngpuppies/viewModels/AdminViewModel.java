package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Admin;

public class AdminViewModel {

    public UserViewModel adminCredentials;

    public String email;

    public static AdminViewModel fromModel(Admin admin) {
        AdminViewModel vm = new AdminViewModel();

        if(admin != null) {
            vm.adminCredentials = UserViewModel.fromModel(admin);
            vm.email = admin.getEmail();
        }

        return vm;
    }
}