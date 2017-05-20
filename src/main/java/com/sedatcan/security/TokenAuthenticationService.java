package com.sedatcan.security;

import com.sedatcan.entity.Customer;
import com.sedatcan.exception.ToDoListErrorCode;
import com.sedatcan.exception.ToDoListException;
import com.sedatcan.model.CustomerDto;
import com.sedatcan.service.CustomerService;
import com.sedatcan.service.Tokenizer;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class TokenAuthenticationService implements AuthenticationProvider {

    private static final String CLAIMS_MAP_CUSTOMER_ID = "customer_id";
    private static final String AUTH_HEADER = "X-CLIENT-TOKEN";

    @Autowired
    private Tokenizer tokenizer;

    @Autowired
    private CustomerService customerService;

    private long expiryTimeoutInMinutes = 60;

    private String secret = "todolistappsecret";

    public String generateToken(Customer customer) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(CLAIMS_MAP_CUSTOMER_ID, customer.getId());
        return tokenizer.generateToken(customer.getId(), expiryTimeoutInMinutes, SignatureAlgorithm.HS512, secret, claimsMap);
    }

    public String generateToken(CustomerDto customer) {
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(CLAIMS_MAP_CUSTOMER_ID, customer.getId());
        return tokenizer.generateToken(customer.getId(), expiryTimeoutInMinutes, SignatureAlgorithm.HS512, secret, claimsMap);
    }

    public void addAuthentication(HttpServletResponse response, Authentication authentication) {
        if (authentication == null || !(authentication instanceof CustomerDto) || !authentication.isAuthenticated()) {
            throw new ToDoListException(ToDoListErrorCode.UNAUTHORIZED_REQUEST, HttpStatus.UNAUTHORIZED);
        }
        CustomerDto customerDto = (CustomerDto) authentication;
        try {
            String token = generateToken(customerDto);
            response.addHeader("X-CLIENT-TOKEN", token);
            response.getWriter().write(token);
            response.getWriter().flush();
            response.getWriter().close();
        }catch (IOException e){
            throw new ToDoListException(ToDoListErrorCode.UNAUTHORIZED_REQUEST,HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = getAuthTokenHeader(request);

        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            String customerId = tokenizer.parseTokenAndClaim(secret, token, CLAIMS_MAP_CUSTOMER_ID, String.class);
            return customerService.getById(customerId);
        } catch (Exception e) {
            return null;
        }
    }

    private String getAuthTokenHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomerDto.class.isAssignableFrom(authentication);

    }
}
