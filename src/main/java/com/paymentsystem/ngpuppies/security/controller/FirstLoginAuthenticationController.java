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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Validated
@RestController
@RequestMapping()
public class FirstLoginAuthenticationController {
    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasRole('ROLE_INITIAL')")
    @GetMapping("/first-login")
    public PasswordResetDto getFirstLoginTemplate() {
        return new PasswordResetDto();
    }

    @PreAuthorize("hasRole('ROLE_INITIAL')")
    @PostMapping("/first-login")
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

    @PostMapping(value = "/password-reset")
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestParam("email") String email) {
         Admin admin =  adminService.loadByEmail(email);
        if (admin == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String token = UUID.randomUUID().toString();

        if(adminService.createPasswordResetTokenForUser(admin, token)) {
//            mailSender.send(constructResetTokenEmail(getAppUrl(request),
//                    request.getLocale(), token, admin));


        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
