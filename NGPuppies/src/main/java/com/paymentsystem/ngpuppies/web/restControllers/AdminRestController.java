package com.paymentsystem.ngpuppies.web.restControllers;

import com.paymentsystem.ngpuppies.models.Admin;
import com.paymentsystem.ngpuppies.models.Client;
import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
import com.paymentsystem.ngpuppies.services.ApplicationUserServiceImpl;
import com.paymentsystem.ngpuppies.services.ClientServiceImpl;
import com.paymentsystem.ngpuppies.viewModels.AdminViewModel;
import com.paymentsystem.ngpuppies.viewModels.ClientViewModel;
import com.paymentsystem.ngpuppies.viewModels.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/secured/admin")
public class AdminRestController {

    @Autowired
    private ApplicationUserServiceImpl applicationUserService;
    @Autowired
    private AdminServiceImpl adminService;
    @Autowired
    private ClientServiceImpl clientService;

    @GetMapping("/user")
    public UserViewModel getUserByUsername(@RequestParam String username) {
        return UserViewModel.fromModel(applicationUserService.getByUsername(username));
    }

    @GetMapping("/{username}")
    public AdminViewModel getAdminByUsername(@PathVariable("username") String username) {
        return AdminViewModel.fromModel(adminService.getByUsername(username));
    }

    @GetMapping("/client")
    public ClientViewModel getClientByUsername(@RequestParam String username) {
        return ClientViewModel.fromModel(clientService.getByUsername(username));
    }

    @GetMapping("/all")
    public List<AdminViewModel> getAllAdmins() {
        return adminService.getAll().stream()
                .map(AdminViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/clients/all")
    public List<ClientViewModel> getAllClients() {
        return clientService.getAll().stream()
                .map(ClientViewModel::fromModel)
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
        //TODO encrypt password using BCrypt
        // admin.setPassword(admin.getPassword());
        return adminService.create(admin);
    }

    @PostMapping("/client/register")
    public boolean registerClient(@RequestBody Client client) {
        if (applicationUserService.checkIfUsernameIsPresent(client.getUsername())) {
            return false;
        }
        if (clientService.checkIfEikIsPresent(client.getEik())) {
            return false;
        }

        //TODO encrypt password using BCrypt
        // client.setPassword(client.getPassword());

        return clientService.create(client);
    }

    @DeleteMapping("/delete")
    public boolean deleteUserByUsername(@RequestParam() String username) {
        return applicationUserService.deleteByUsername(username);
    }
}