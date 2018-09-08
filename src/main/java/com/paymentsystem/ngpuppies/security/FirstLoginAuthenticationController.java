package com.paymentsystem.ngpuppies.security;

import com.paymentsystem.ngpuppies.models.dto.PasswordResetDТО;
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
    public PasswordResetDТО getFirstLoginTemplate() {
        return new PasswordResetDТО();
    }

    @PostMapping()
    public ResponseEntity<?> firstLogin(@RequestBody @Valid PasswordResetDТО passwordResetDТО,
                                        Authentication authentication) {
        try {
            Admin admin = (Admin) authentication.getPrincipal();
            adminService.updateAfterFirstLogin(admin, passwordResetDТО);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
