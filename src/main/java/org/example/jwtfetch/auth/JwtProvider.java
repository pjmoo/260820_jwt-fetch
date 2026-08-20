package org.example.jwtfetch.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.jwtfetch.config.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@EnableConfigurationProperties(AuthProperties.class)
@RequiredArgsConstructor
public class JwtProvider {
    private final AuthProperties p;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(p.jwt().secretKey().getBytes());
    }

    public String issueToken(String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime()
                + p.jwt().accessTokenExpiry().toMillis());
        return Jwts.builder()
                .signWith(getSecretKey())
                .issuedAt(now)
                .expiration(expiration)
                .subject(username)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token) // exception
                .getPayload();
    }
}
