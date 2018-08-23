//package com.paymentsystem.ngpuppies.security;
//
//import com.paymentsystem.ngpuppies.models.users.ApplicationUser;
//import com.paymentsystem.ngpuppies.models.users.Authority;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public final class JwtUserFactory {
//
//    private JwtUserFactory() {
//    }
//
//    public static JwtUser create(ApplicationUser user) {
//        return new JwtUser(
//                user.getId(),
//                user.getUsername(),
//                user.getPassword(),
//                mapToGrantedAuthorities(user.getAuthority()),
//                user.getEnabled(),
//                user.getLastPasswordResetDate()
//        );
//    }
//
//    private static List<GrantedAuthority> mapToGrantedAuthorities(Authority authority) {
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(authority.getName().name()));
//        return authorities;
//    }
//}
