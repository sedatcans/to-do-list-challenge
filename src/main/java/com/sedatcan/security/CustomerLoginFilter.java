package com.sedatcan.security;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedatcan.model.CustomerDto;
import com.sedatcan.model.LoginDto;
import com.sedatcan.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomerLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public CustomerLoginFilter(String urlMapping, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authManager);
    }
    private static final String ORIGIN = "Origin";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String origin = httpServletRequest.getHeader(ORIGIN);
        httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.addHeader("Access-Control-Allow-Headers",
                httpServletRequest.getHeader("Access-Control-Request-Headers"));
        if (httpServletRequest.getMethod().equals( "OPTIONS")) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            httpServletResponse.flushBuffer();
            return CustomerDto.builder().build();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

        final LoginDto loginDto = objectMapper.readValue(httpServletRequest.getInputStream(), LoginDto.class);
        CustomerDto customer = customerService.getCustomerByEmail(loginDto.getEmail());
        if (customer != null) {
            if (bCryptPasswordEncoder.matches(loginDto.getPassword(), customer.getPassword())) {
                return customer;
            }
        }
        throw new InternalAuthenticationServiceException("Invalid credentials");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication)
            throws IOException, ServletException {
        tokenAuthenticationService.addAuthentication(response, authentication);
    }
}
