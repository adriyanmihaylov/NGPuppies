package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.*;
import com.paymentsystem.ngpuppies.models.dto.*;
import com.paymentsystem.ngpuppies.models.users.*;
import com.paymentsystem.ngpuppies.services.base.*;
import com.paymentsystem.ngpuppies.viewModels.*;
import com.paymentsystem.ngpuppies.web.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}")
public class AdminUsersController {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResponseHandler responseHandler;

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
                                                  BindingResult bindingResult,
                                                  Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return responseHandler.bindingResultHandler(bindingResult);
        }
        try {
            Admin admin = (Admin) authentication.getPrincipal();
            if (!adminDTO.getUsername().equals(admin.getUsername())) {
                return responseHandler.returnResponse("Invalid credentials! ", HttpStatus.UNAUTHORIZED);
            }
            admin.setUsername(adminDTO.getUsername());
            admin.setEmail(adminDTO.getEmail());

            if (adminDTO.getPassword() != null) {
                admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
                admin.setLastPasswordResetDate(new Date());
            }

            if (!adminService.update(admin)) {
                return responseHandler.returnResponse("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return responseHandler.returnResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return responseHandler.returnResponse("Please try again later", HttpStatus.BAD_REQUEST);
        }
        return responseHandler.returnResponse("Account updated", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserViewModel> getUserByUsername(@RequestParam("username") String username) {
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

    @DeleteMapping("/user/delete")
    public ResponseEntity<Response> deleteUserByUsername(@RequestParam() String username) {
        User user = (User) userService.loadUserByUsername(username);
        if (user != null) {
            if (userService.delete(user)) {
                return responseHandler.returnResponse("User " + username + " deleted successfully", HttpStatus.OK);

            }
            return responseHandler.returnResponse("Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseHandler.returnResponse("User not found!", HttpStatus.BAD_REQUEST);
    }
}