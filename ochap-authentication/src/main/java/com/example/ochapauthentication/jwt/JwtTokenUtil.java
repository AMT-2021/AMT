package com.example.ochapauthentication.jwt;

import com.example.ochapauthentication.dto.AccountInfoDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil implements Serializable {
    public static final long JWT_TOKEN_VALIDITY = 5*60*60;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(AccountInfoDTO userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getRole());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] apiKeySecretBytes = secret.getBytes();
        Key signingKey = new SecretKeySpec(apiKeySecretBytes,signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("IICT")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY*1000)).signWith(signatureAlgorithm, signingKey)
                .compact();
    }
}
