package com.paymentsystem.ngpuppies.security.controller;

import com.paymentsystem.ngpuppies.models.IpAddress;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.security.JwtAuthenticationRequest;
import com.paymentsystem.ngpuppies.security.JwtTokenUtil;
import com.paymentsystem.ngpuppies.services.base.UserService;
import com.paymentsystem.ngpuppies.validation.exceptions.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    final UserService userService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid  JwtAuthenticationRequest authenticationRequest,HttpServletRequest request,
                                                       BindingResult bindingResult) throws AuthenticationException {
        Map<String, Object> map = new HashMap<>();
        final User user;
        final String token;

        if (authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword())) {

            // Reload password post-security so we can generate the token
            user = (User) userService.loadUserByUsername(authenticationRequest.getUsername());
            if(user.getAuthority().getName() != AuthorityName.ROLE_CLIENT) {
                IpAddress ipAddress = new IpAddress(request.getRemoteAddr());
                if (user.getIpAddresses().size() == 0) {
                    // Save current Ip address
                    userService.addIpAddress(user, ipAddress);
                } else if (!user.getIpAddresses().contains(ipAddress)) {
                    throw new DisabledException("Invalid IP address!");
                }
            }

            token = jwtTokenUtil.generateToken(user, false);

            map.put("token", token);

            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            user = (User) userService.loadUserByUsername(authenticationRequest.getUsername());

            if (user.getAuthority().getName().equals(AuthorityName.ROLE_INITIAL)) {
                token = jwtTokenUtil.generateToken(user, true);
                map.put("token", token);
            } else {
                throw new DisabledException("User is disabled");
            }
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<String> handleDisabledException(DisabledException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    private boolean authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            return false;
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }

        return true;
    }
}
