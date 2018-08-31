package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
import com.paymentsystem.ngpuppies.services.ApplicationUserServiceImpl;
import com.paymentsystem.ngpuppies.viewModels.AdminViewModel;
import com.paymentsystem.ngpuppies.viewModels.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
public class AdminRestController {

    @Autowired
    private ApplicationUserServiceImpl applicationUserService;
    @Autowired
    private AdminServiceImpl adminService;


    @GetMapping("/user")
    public UserViewModel getUserByUsername(@RequestParam String username) {
        return UserViewModel.fromModel(applicationUserService.getByUsername(username));
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


<<<<<<< HEAD:NGPuppies/src/main/java/com/paymentsystem/ngpuppies/web/restControllers/AdminRestController.java
    @GetMapping("/all")
    public List<AdminViewModel> getAllAdmins() {
        return adminService.getAll().stream()
                .map(AdminViewModel::fromModel)
=======
    @GetMapping("/client")
    public ClientViewModel getClientByUsername(@RequestParam String username) {
        return ClientViewModel.fromModel(clientService.getByUsername(username));
    }

    @GetMapping("/allUsers")
    public List<UserViewModel> getAllUsers() {
        return applicationUserService.getAll().stream()
                .map(UserViewModel::fromModel)
>>>>>>> 8d1a9b6ed4555a22aaa42a53312f1e9c96c94e74:NGPuppies/src/main/java/com/paymentsystem/ngpuppies/web/AdminRestController.java
                .collect(Collectors.toList());
    }

    /**
     * Try with
     * POST          http://localhost:8080/secured/admin/register
     * {
     * "username": "username",
     * "password": "123456",
     * "email": "email@yahoo.com"
     * }
     */
    @PostMapping("/register")
    public boolean registerAdmin(@RequestBody Admin admin) {
        if (applicationUserService.checkIfUsernameIsPresent(admin.getUsername())) {
            return false;
        }
        if (adminService.checkIfEmailIsPresent(admin.getEmail())) {
            return false;
        }
        Admin admin1 = new Admin(admin.getUsername(), admin.getPassword(), admin.getEmail());
        //TODO encrypt password using BCrypt
        admin.setPassword(admin.getPassword());
        return adminService.create(admin1);
    }



    @DeleteMapping("/delete")
    public boolean deleteUserByUsername(@RequestParam() String username) {
        return applicationUserService.deleteByUsername(username);
    }
}