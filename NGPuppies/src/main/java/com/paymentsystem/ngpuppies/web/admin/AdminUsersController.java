package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.dto.*;
import com.paymentsystem.ngpuppies.models.users.*;
import com.paymentsystem.ngpuppies.services.base.*;
import com.paymentsystem.ngpuppies.models.viewModels.*;
import com.paymentsystem.ngpuppies.validator.base.ValidUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}")
@Validated()
public class AdminUsersController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/account")
    public ResponseEntity<AdminViewModel> getAccount(Authentication authentication) {
        AdminViewModel viewModel = AdminViewModel.fromModel(adminService.loadByUsername(authentication.getName()));

        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/account/update")
    public ResponseEntity<Response> updateAccount(@Valid @RequestBody AdminDTO adminDTO,
                                                  Authentication authentication,
                                                  BindingResult bindingResult) {
        try {
            Admin admin = (Admin) authentication.getPrincipal();
            if (!adminDTO.getUsername().equals(admin.getUsername())) {
                return new ResponseEntity<>(new Response("Invalid credentials!"), HttpStatus.UNAUTHORIZED);
            }
            admin.setUsername(adminDTO.getUsername());
            admin.setEmail(adminDTO.getEmail());

            if (adminDTO.getPassword() != null) {
                admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
                admin.setLastPasswordResetDate(new Date());
            }

            if (!adminService.update(admin)) {
                return new ResponseEntity<>(new Response("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response("Please try again later"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response("Account updated"), HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserViewModel> getUserByUsername(@RequestParam("username") @ValidUsername String username) {
        UserViewModel viewModel = UserViewModel.fromModel((User) userService.loadUserByUsername(username));
        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<UserViewModel>> getAllUsers() {
        List<UserViewModel> viewModels = userService.getAll().stream()
                .map(UserViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/user/{username}/delete")
    public ResponseEntity<Response> deleteUserByUsername(@PathVariable("username") @ValidUsername String username) {
        User user = (User) userService.loadUserByUsername(username);
        if (user != null) {
            if (userService.delete(user)) {
                return new ResponseEntity<>(new Response("User " + username + " deleted successfully"), HttpStatus.OK);

            }
            return new ResponseEntity<>(new Response("Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Response("User not found!"), HttpStatus.BAD_REQUEST);
    }
}