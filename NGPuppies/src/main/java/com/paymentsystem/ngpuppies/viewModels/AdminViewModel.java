package com.paymentsystem.ngpuppies.viewModels;


import com.paymentsystem.ngpuppies.models.users.Admin;

public class AdminViewModel {

    public UserViewModel credentials;

    public String email;

    public static AdminViewModel fromModel(Admin admin) {
        AdminViewModel vm = new AdminViewModel();

        if(admin != null) {
            vm.credentials = UserViewModel.fromModel(admin);
            vm.email = admin.getEmail();
        }

        return vm;
    }
}