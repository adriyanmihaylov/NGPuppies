package com.paymentsystem.ngpuppies.models.viewModels;


import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;

public class AdminViewModel {

    public String username;

    public String email;

    public AuthorityName authority;

    public static AdminViewModel fromModel(Admin admin) {
        AdminViewModel vm = new AdminViewModel();

        if(admin != null) {
            vm.username = admin.getUsername();
            vm.email = admin.getEmail();
            vm.authority = admin.getAuthority().getName();
        }

        return vm;
    }
}