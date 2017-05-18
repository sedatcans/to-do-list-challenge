package com.sedatcan.service.impl;

import com.sedatcan.common.BaseServiceTest;
import com.sedatcan.model.CustomerDto;
import com.sedatcan.security.TokenAuthenticationService;
import com.sedatcan.service.CustomerService;
import com.sedatcan.service.Tokenizer;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TokenAuthenticationService.class)
public class TokenAuthenticationServiceImplTest extends BaseServiceTest {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private Tokenizer tokenizer;

    private String token = "I am Token";

    @Test
    public void addAuthenticationAnySuccessTest() {
        String customerId = "Customer1";
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        CustomerDto customerDto = CustomerDto.builder().id(customerId).build();

        HashMap<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("customer_id", "Customer1");
        when(tokenizer.generateToken("Customer1", 60L, SignatureAlgorithm.HS512, "todolistappsecret", claimsMap)).thenReturn("generated-token-123qwe");

        tokenAuthenticationService.addAuthentication(httpServletResponse, customerDto);

        assertThat(httpServletResponse.getHeader("X-CLIENT-TOKEN"), notNullValue());
        verify(tokenizer).generateToken(anyString(), any(), any(), anyString(), any());
    }

    @Test
    public void addAuthenticationSuccessTest() {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        String customerId = "Customer1";
        CustomerDto customerDto = CustomerDto.builder().id(customerId).build();
        HashMap<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("customer_id", "Customer1");

        when(tokenizer.generateToken("Customer1", 60L, SignatureAlgorithm.HS512, "todolistappsecret", claimsMap)).thenReturn("generated-token-123qwe");
        tokenAuthenticationService.addAuthentication(httpServletResponse, customerDto);

        assertThat(httpServletResponse.getHeader("X-CLIENT-TOKEN"), equalTo("generated-token-123qwe"));
        verify(tokenizer).generateToken("Customer1", 60L, SignatureAlgorithm.HS512, "todolistappsecret", claimsMap);
    }

    @Test
    public void getAuthenticationByToken() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("X-CLIENT-TOKEN", token);

        when(tokenizer.parseTokenAndClaim("todolistappsecret", token, "customer_id",String.class)).thenReturn("Customer1");
        when(customerService.getById("Customer1")).thenReturn(CustomerDto.builder().id("Customer1").build());
        CustomerDto authentication = (CustomerDto) tokenAuthenticationService.getAuthentication(mockHttpServletRequest);

        assertThat(authentication.isAuthenticated(), equalTo(true));
        verify(customerService).getById("Customer1");
        verify(tokenizer).parseTokenAndClaim("todolistappsecret", token, "customer_id",String.class);
    }

}
