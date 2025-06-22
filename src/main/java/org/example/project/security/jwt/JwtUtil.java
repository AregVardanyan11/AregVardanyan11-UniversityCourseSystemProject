package org.example.project.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class JwtUtil {

    public static final long JWT_TOKEN_VALIDITY = 1000L * 60 * 60; // 1 hour
    public static final long JWT_REFRESH_TOKEN_VALIDITY = 1000L * 60 * 60 * 24 * 30; // 30 days
    public static final String AUTH_TYPE = "Bearer ";
    private static final String ROLES = "roles";

    @Value("${jwt.secret}")
    private String SECRET;

    public String generateAccessToken(UserPrincipal userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .withIssuedAt(Instant.now())
                .withClaim(ROLES, userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(this.getAlgorithm());
    }

    public String generateRefreshToken(UserPrincipal userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY))
                .withIssuedAt(Instant.now())
                .sign(this.getAlgorithm());
    }

    public String getUsername(String token) {
        return this.verifyAndDecode(token).getSubject();
    }

    public String[] getAuthorities(String token) {
        return this.verifyAndDecode(token).getClaim(ROLES).asArray(String.class);
    }

    public boolean isVerified(String token) {
        try {
            this.verifyAndDecode(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET.getBytes(UTF_8));
    }

    private DecodedJWT verifyAndDecode(String token) {
        return JWT.require(this.getAlgorithm()).build().verify(token);
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}