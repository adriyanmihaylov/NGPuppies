package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.exceptions.EikIsPresentException;
import com.paymentsystem.ngpuppies.exceptions.EmailIsPresentException;
import com.paymentsystem.ngpuppies.exceptions.UsernameIsPresentException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
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
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminDto adminDto, BindingResult bindingResult) throws UsernameIsPresentException, EmailIsPresentException {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return ResponseEntity.badRequest().body(message);
        }
        try {
            Admin admin = new Admin();
            admin.setUsername(adminDto.getUsername());
            admin.setPassword(adminDto.getPassword());
            admin.setEmail(adminDto.getEmail());
            Authority authority = authorityService.getByName(AuthorityName.ROLE_ADMIN);
            admin.setAuthority(authority);

            adminService.create(admin);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Please try again later");
        }

        return ResponseEntity.ok("Successful registration!");
    }

    @DeleteMapping("/delete")
    public boolean deleteUserByUsername(@RequestParam() String username) {
        return appUserService.deleteByUsername(username);
    }
//
//    @ExceptionHandler({EmailIsPresentException.class,UsernameIsPresentException.class,EikIsPresentException.class})
//    public ResponseEntity<String> handleCredentialsArePresentExceptions(EmailIsPresentException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//    }
}