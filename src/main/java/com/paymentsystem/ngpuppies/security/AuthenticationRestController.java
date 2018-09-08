package com.paymentsystem.ngpuppies.security;

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

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserService userService;
    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody @Valid  JwtAuthenticationRequest authenticationRequest,
                                                       BindingResult bindingResult) throws AuthenticationException {
        Map<String, Object> map = new HashMap<>();
        final User user;
        final String token;

        if (authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword())) {

            // Reload password post-security so we can generate the token
            user = (User) userService.loadUserByUsername(authenticationRequest.getUsername());
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


    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);

        Integer id = jwtTokenUtil.getIdFromToken(token);
        User user = userService.loadById(id);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(refreshedToken);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
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
