package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.datatransferobjects.AdminDto;
import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import com.paymentsystem.ngpuppies.services.base.AppUserService;
import com.paymentsystem.ngpuppies.services.base.AuthorityService;
import com.paymentsystem.ngpuppies.viewModels.AdminViewModel;
import com.paymentsystem.ngpuppies.viewModels.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
public class AdminRestController {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthorityService authorityService;


    @GetMapping("/user")
    public UserViewModel getUserByUsername(@RequestParam String username) {
        return UserViewModel.fromModel(appUserService.loadByUsername(username));
    }

    @GetMapping("/{username}")
    public AdminViewModel getAdminByUsername(@PathVariable("username") String username) {
        return AdminViewModel.fromModel(adminService.getByUsername(username));
    }
    @GetMapping("/all")
    public List<AdminViewModel> getAllAdmins() {
        return adminService.getAll().stream()
                .map(AdminViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/allUsers")
    public List<UserViewModel> getAllUsers() {
        return appUserService.getAll().stream()
                .map(UserViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @PostMapping("/register-admin")
    public boolean registerAdmin(@Valid @RequestBody AdminDto adminDto) {
        Admin admin = new Admin();
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(adminDto.getPassword());
        admin.setEmail(adminDto.getEmail());
        Authority authority = authorityService.getByName(AuthorityName.ROLE_ADMIN);
        admin.setAuthority(authority);

        return adminService.create(admin);
    }

    @DeleteMapping("/delete")
    public boolean deleteUserByUsername(@RequestParam() String username) {
        return appUserService.deleteByUsername(username);
    }
}