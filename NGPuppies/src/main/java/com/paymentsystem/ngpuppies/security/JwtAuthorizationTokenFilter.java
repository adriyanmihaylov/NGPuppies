package com.paymentsystem.ngpuppies.security;

import com.paymentsystem.ngpuppies.models.users.AppUser;
import com.paymentsystem.ngpuppies.services.base.AppUserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader;
    private AppUserService appUserService;

    public JwtAuthorizationTokenFilter(AppUserService appUserService,JwtTokenUtil jwtTokenUtil,
                                       @Value("${jwt.header}") String tokenHeader) {
        this.appUserService = appUserService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = tokenHeader;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.debug("processing authentication for '{}'", request.getRequestURL());

        final String requestHeader = request.getHeader(this.tokenHeader);
        Integer id = null;
        String authToken = null;
        try {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                try {
                    id = jwtTokenUtil.getIdFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    logger.error("An error occured during getting username from token");
                    logger.warn(e.getMessage());
                } catch (ExpiredJwtException e) {
                    logger.warn("The token is expired and not valid anymore");
                    logger.warn(e.getMessage());
                }
            } else {
//                logger.warn("couldn't find bearer string, will ignore the header");
            }

            logger.debug("Checking authentication for user '{}'", id);
            if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.debug("Security context was null, so authorizating user");

                // It is not compelling necessary to load the use details from the database. You could also store the information
                // in the token and read it from it. It's up to you ;)
                AppUser appUser = null;
                try {
                    appUser = appUserService.loadById(id);

                    // For simple validation it is completely sufficient to just check the token integrity.
                    // You don't have to call the database compellingly.
                    if (jwtTokenUtil.validateToken(authToken, appUser)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(appUser, null, appUser.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        logger.info("Authorizated user '{}', setting security context", id);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (UsernameNotFoundException e) {
                    System.out.println("Token parsing error: " + e.getMessage());
                }
                // For simple validation it is completely sufficient to just check the token integrity.
                // You don't have to call the database compellingly.
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}