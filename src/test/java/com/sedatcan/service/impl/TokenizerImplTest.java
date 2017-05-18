package com.sedatcan.service.impl;

import com.sedatcan.common.BaseServiceTest;
import com.sedatcan.service.Tokenizer;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@SpringBootTest(classes = TokenizerImpl.class)
public class TokenizerImplTest extends BaseServiceTest {

    @Autowired
    private Tokenizer tokenizer;

    @Value("${security.jwt.secureKey}")
    private String secret;

    @Test
    public void shouldGenerateTokenWithJwtAndParseWhenClaimsAreInteger() {
        HashMap<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("customerId", "Customer1");

        String token = tokenizer.generateToken("Customer1", 60L, SignatureAlgorithm.HS512, secret, claimsMap);

        assertThat(token, not(isEmptyString()));
        assertThat(tokenizer.parseTokenAndClaim(secret, token, "customerId", String.class), equalTo("Customer1"));
    }
}