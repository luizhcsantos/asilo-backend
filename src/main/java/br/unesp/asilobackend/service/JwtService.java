package br.unesp.asilobackend.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-expiration-minutes}")
    private int accessExpirationMinutes;

    private Algorithm algorithm;
    private JWTVerifier verifier;

    @PostConstruct
    public void init() {
        algorithm = Algorithm.HMAC256(jwtSecret);
        verifier = JWT.require(algorithm).build();
    }

    public String generateAccessToken(String subject) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpirationMinutes * 60L);

        return JWT.create()
                .withSubject(subject)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }

    public String generateAccessToken(String subject, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessExpirationMinutes * 60L);

        return JWT.create()
                .withSubject(subject)
                .withClaim("role", role)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .sign(algorithm);
    }

    public DecodedJWT verify(String token) {
        return verifier.verify(token);
    }
}
