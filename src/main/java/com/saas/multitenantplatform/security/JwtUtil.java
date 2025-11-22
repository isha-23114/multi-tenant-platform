package com.saas.multitenantplatform.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expiryMillis;

    public JwtUtil(String secret, long expiryMillis) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.expiryMillis = expiryMillis;
    }

    public String generateToken(String subject, String tenant, String email){
        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject(subject)
                .withClaim("tenant",tenant)
                .withClaim("email",email)
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + expiryMillis))
                .sign(algorithm);
    }

    public DecodedJWT validateToken(String token){
        return verifier.verify(token);
    }
}
