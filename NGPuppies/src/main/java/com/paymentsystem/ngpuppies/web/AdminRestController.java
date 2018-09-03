package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.datatransferobjects.AdminDto;
import com.paymentsystem.ngpuppies.models.datatransferobjects.ClientDto;
import com.paymentsystem.ngpuppies.models.users.*;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import com.paymentsystem.ngpuppies.services.base.AppUserService;
import com.paymentsystem.ngpuppies.services.base.AuthorityService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.viewModels.AdminViewModel;
import com.paymentsystem.ngpuppies.viewModels.ClientViewModel;
import com.paymentsystem.ngpuppies.viewModels.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
//@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}")
public class AdminRestController {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user")
    public UserViewModel getUserByUsername(@RequestParam("username") String username) {
        return UserViewModel.fromModel((AppUser) appUserService.loadUserByUsername(username));
    }

    @GetMapping("/admin")
    public AdminViewModel getAdminByUsername(@RequestParam("username") String username) {
        return AdminViewModel.fromModel(adminService.loadByUsername(username));
    }

    @GetMapping("/client")
    public ClientViewModel getClientByUsername(@RequestParam("username") String username) {
        return ClientViewModel.fromModel(clientService.loadByUsername(username));
    }

    @GetMapping("/get/users")
    public List<UserViewModel> getAllUsers() {
        return appUserService.getAll().stream()
                .map(UserViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/get/admins")
    public List<AdminViewModel> getAllAdmins() {
        return adminService.getAll().stream()
                .map(AdminViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/get/clients")
    public List<ClientViewModel> getAllClients() {
        return clientService.getAll().stream()
                .map(ClientViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/account")
    public AdminViewModel getAccount(Authentication authentication) {
        return AdminViewModel.fromModel(adminService.loadByUsername(authentication.getName()));
    }

    @PutMapping("/account")
    public ResponseEntity updateAccount(@Valid @RequestBody AdminDto adminDto, BindingResult bindingResult,Authentication authentication) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();

            return ResponseEntity.badRequest().body(message);
        }
        try {
            Admin admin = (Admin) authentication.getPrincipal();
            admin.setUsername(adminDto.getUsername());
            admin.setEmail(adminDto.getEmail());

            if (adminDto.getPassword() != null) {
                admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
            }

            if (!adminService.update(admin)) {
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }
        return ResponseEntity.ok("Account updated!");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminDto adminDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        if(adminDto.getPassword() == null) {
            return ResponseEntity.badRequest().body("Password can not be empty!");
        }
        try {
            Admin admin = new Admin();
            admin.setUsername(adminDto.getUsername());
            admin.setPassword(adminDto.getPassword());
            admin.setEmail(adminDto.getEmail());
            Authority authority = authorityService.getByName(AuthorityName.ROLE_ADMIN);
            admin.setAuthority(authority);

            if (!adminService.create(admin)) {
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }

        return ResponseEntity.ok("Successful registration!");
    }

    @PostMapping("/register/client")
    public ResponseEntity<?> registerClient(@Valid @RequestBody ClientDto clientDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        if(clientDto.getPassword() == null) {
            return ResponseEntity.badRequest().body("Password can not be empty!");
        }
        try {
            Client client = new Client();
            client.setUsername(clientDto.getUsername());
            client.setPassword(clientDto.getPassword());
            client.setEik(clientDto.getEik());
            Authority authority = authorityService.getByName(AuthorityName.ROLE_CLIENT);
            client.setAuthority(authority);

            if (!clientService.create(client)) {
                return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
            }
        } catch (SQLException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Please try again later!");
        }

        return ResponseEntity.ok("Successful registration!");
    }

    @DeleteMapping("/user/delete")
    public boolean deleteUserByUsername(@RequestParam() String username) {
        return appUserService.deleteByUsername(username);
    }
}