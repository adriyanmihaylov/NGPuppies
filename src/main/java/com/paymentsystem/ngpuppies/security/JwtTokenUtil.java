package com.paymentsystem.ngpuppies.security;

import com.paymentsystem.ngpuppies.models.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;
    private Clock clock = DefaultClock.INSTANCE;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.first.login.expiration}")
    private Long firstLoginExpiration;

    public Integer getIdFromToken(String token) {
        return Integer.parseInt(getClaimFromToken(token, Claims::getSubject));
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(User user,boolean isFirstLogin) {
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", user.getAuthorities());
        return doGenerateToken(claims, String.valueOf(user.getId()),isFirstLogin);
    }

    private String doGenerateToken(Map<String,Object> claims, String subject,boolean isFirstLogin) {
        final Date createdDate = clock.now();
        final Date expirationDate;
        if(isFirstLogin) {
            expirationDate = calculateFirstLoginExpirationDate(createdDate);
        } else {
            expirationDate = calculateExpirationDate(createdDate);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, User user) {
        if(user == null) {
            return false;
        }

        final Integer id = getIdFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);

        return (
                id == user.getId()
                        && !isTokenExpired(token)
                        && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
        );
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

    private Date calculateFirstLoginExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + firstLoginExpiration * 1000);
    }
}
