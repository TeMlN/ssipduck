package com.server.ssipduck.global.security.provider;

import com.server.ssipduck.domain.user.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    @Value("${auth.secret-key}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String generateAccessToken(String username, List<Role> roles) {
        final String AUTHORITIES_KEY = "auth";
        final long ACCESS_TOKEN_EXPIRED_MILLI_SECOND = 1000L * 60 * 60 * 3;

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTHORITIES_KEY, roles.stream()
                .map(Role::getAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        Date date = new Date();
        Date accessTokenExpiration = new Date(date.getTime() + ACCESS_TOKEN_EXPIRED_MILLI_SECOND);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(accessTokenExpiration)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

    }

}
