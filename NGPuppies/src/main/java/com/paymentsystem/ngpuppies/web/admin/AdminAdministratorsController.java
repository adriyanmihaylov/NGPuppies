package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.dto.Response;
import com.paymentsystem.ngpuppies.models.dto.AdminDTO;
import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.services.base.*;
import com.paymentsystem.ngpuppies.models.viewModels.AdminViewModel;
import com.paymentsystem.ngpuppies.validator.base.ValidUsername;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("${common.basepath}/admin")
@Validated
public class AdminAdministratorsController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthorityService authorityService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{username}")
    public ResponseEntity<AdminViewModel> getAdminByUsername(@PathVariable("username") @ValidUsername String username) {
        AdminViewModel viewModel = AdminViewModel.fromModel(adminService.loadByUsername(username));

        if (viewModel != null) {
            return new ResponseEntity<>(viewModel, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AdminViewModel>> getAllAdmins() {
        List<AdminViewModel> viewModels = adminService.getAll().stream()
                .map(AdminViewModel::fromModel)
                .collect(Collectors.toList());

        if (viewModels != null) {
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/register")
    public AdminDTO registerAdmin() {
        return new AdminDTO();
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerAdmin(@RequestBody @Valid AdminDTO adminDTO,
                                                  BindingResult bindingResult) {
        if (adminDTO.getPassword() == null) {
            return new ResponseEntity<>(new Response("Missing password!"), HttpStatus.BAD_REQUEST);
        }
        try {
            Authority authority = authorityService.getByName(AuthorityName.ROLE_ADMIN);
            Admin admin = new Admin(adminDTO.getUsername(), adminDTO.getPassword(), adminDTO.getEmail(), authority);

            if (!adminService.create(admin)) {
                return new ResponseEntity<>(new Response("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (SQLException e) {
            return new ResponseEntity<>(new Response(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new Response("Please try again later"), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Response("Successful registration!"), HttpStatus.OK);
    }


    @PutMapping("/update")
    public ResponseEntity<Response> updateAdmin(@RequestParam @ValidUsername String username,
                                                @RequestBody @Valid AdminDTO adminDTO,
                                                BindingResult bindingResult) {
        try {
            Admin admin = adminService.loadByUsername(username);
            if (admin == null) {
                return new ResponseEntity<>(new Response(adminDTO.getUsername() + " was not found!"), HttpStatus.BAD_REQUEST);
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

        return new ResponseEntity<>(new Response("Successful update!"), HttpStatus.OK);
    }
}