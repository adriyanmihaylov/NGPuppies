package com.paymentsystem.ngpuppies.security.controller;

import com.paymentsystem.ngpuppies.web.dto.PasswordResetDto;
import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/first-login")
@PreAuthorize("hasRole('ROLE_INITIAL')")
public class FirstLoginAuthenticationController {
    @Autowired
    private AdminService adminService;

    @GetMapping()
    public PasswordResetDto getFirstLoginTemplate() {
        return new PasswordResetDto();
    }

    @PostMapping()
    public ResponseEntity<?> firstLogin(@RequestBody @Valid PasswordResetDto passwordResetDto,
                                        Authentication authentication) {
        try {
            Admin admin = (Admin) authentication.getPrincipal();
            admin.setPassword(passwordResetDto.getPassword());

            adminService.updateOnFirstLogin(admin);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
