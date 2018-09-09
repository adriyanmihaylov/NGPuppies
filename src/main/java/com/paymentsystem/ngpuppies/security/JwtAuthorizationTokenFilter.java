package com.paymentsystem.ngpuppies.security;

import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.security.JwtTokenUtil;
import com.paymentsystem.ngpuppies.services.base.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader;
    private final UserService userService;

    public JwtAuthorizationTokenFilter(UserService userService, JwtTokenUtil jwtTokenUtil,
                                       @Value("${jwt.header}") String tokenHeader) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        final String requestHeader = request.getHeader(this.tokenHeader);
        Integer id = null;
        String authToken = null;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                try {
                    id = jwtTokenUtil.getIdFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    // "An error occured during getting username from token"
                } catch (ExpiredJwtException e) {
                    //"The token is expired and not valid anymore"
                }
            }

            if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // It is not compelling necessary to load the use details from the database.
                // You could also store the information in the token and read it from it. It's up to you ;)
                try {
                   User user = userService.loadById(id);

                    // For simple validation it is completely sufficient to just check the token integrity.
                    //Because we are checking user lastPasswordResetDate we have to load the user
                    if (jwtTokenUtil.validateToken(authToken, user)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (UsernameNotFoundException e) {
                    System.out.println("Token parsing error: " + e.getMessage());
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            //Username changed password while token is valid
        }
    }
}