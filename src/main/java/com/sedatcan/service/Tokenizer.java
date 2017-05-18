package com.sedatcan.service;

import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Map;

public interface Tokenizer {

    String generateToken(String subject, Long expirationTimeInMinutes, SignatureAlgorithm algorithm, String secret, Map<String, Object> claimsMap);

    <T> T parseTokenAndClaim(String secret, String token, String claimName, Class<T> requiredType);

}
