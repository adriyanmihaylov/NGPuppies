package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
import com.paymentsystem.ngpuppies.services.UserServiceImpl;
import com.paymentsystem.ngpuppies.web.dto.*;
import com.paymentsystem.ngpuppies.models.users.*;
import com.paymentsystem.ngpuppies.services.base.*;
import com.paymentsystem.ngpuppies.models.viewModels.*;
import com.paymentsystem.ngpuppies.validation.anotations.ValidUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}")
public class AdminUsersController {
    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public AdminUsersController(UserServiceImpl userService, AdminServiceImpl adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/account")
    public ResponseEntity<AdminViewModel> getAccount(Authentication authentication) {
        AdminViewModel viewModel = AdminViewModel.fromModel(adminService.loadByUsername(authentication.getName()));

        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/account/update")
    public AdminDto getUpdateAccountModel() {
        return new AdminDto();
    }

    @PutMapping("/account/update")
    public ResponseEntity<ResponseMessage> updateAccount(@Valid @RequestBody AdminDto adminDto,
                                                         Authentication authentication,
                                                         BindingResult bindingResult) {
        try {
            Admin admin = (Admin) authentication.getPrincipal();

            if (adminService.update(admin.getUsername(), adminDto)) {
                return new ResponseEntity<>(new ResponseMessage("Account updated"), HttpStatus.OK);
            }
        } catch (IllegalArgumentException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<ResponseMessage> deleteUserByUsername(@PathVariable("username") @ValidUsername String username,
                                                                Authentication authentication) {
        try {
            //Get currently logged user - it must be admin otherwise it has to be unauthorized to deleteCurrencyByName users
            Admin admin = (Admin) authentication.getPrincipal();

            //Check if the currently logged user wants to delete himself
            if (admin.getUsername().equals(username)) {
                return new ResponseEntity<>(new ResponseMessage("You can not delete yourself!"), HttpStatus.BAD_REQUEST);
            }

            if (userService.deleteByUsername(username)) {
                return new ResponseEntity<>(new ResponseMessage(username + " has been deleted!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(new ResponseMessage("User not found!"),HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}