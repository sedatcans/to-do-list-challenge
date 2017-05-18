package com.sedatcan.service.impl;

import com.sedatcan.service.Tokenizer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class TokenizerImpl implements Tokenizer {

    @Override
    public String generateToken(String subject, Long expirationTimeInMinutes, SignatureAlgorithm algorithm, String secret, Map<String, Object> claimsMap) {

        Instant instant = LocalDateTime.now().plusMinutes(expirationTimeInMinutes).toInstant(ZoneOffset.UTC);
        Date expiration = Date.from(instant);
        return Jwts.builder()
                .setClaims(claimsMap)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiration)
                .signWith(algorithm, secret)
                .compact();
    }

    @Override
    public <T> T parseTokenAndClaim(String secret, String token, String claimName, Class<T> requiredType) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .get(claimName, requiredType);
    }
}
