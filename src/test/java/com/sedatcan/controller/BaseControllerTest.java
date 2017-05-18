package com.sedatcan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedatcan.common.BaseSpringTest;
import com.sedatcan.configuration.WebSecurityConfiguration;
import com.sedatcan.model.CustomerDto;
import com.sedatcan.security.TokenAuthenticationService;
import com.sedatcan.service.CustomerService;
import com.sedatcan.service.ToDoListItemService;
import com.sedatcan.service.Tokenizer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
@Import({
        WebSecurityConfiguration.class
})
@MockBean({ToDoListItemService.class,
        CustomerService.class,
        TokenAuthenticationService.class,
        Tokenizer.class,
        BCryptPasswordEncoder.class})
public abstract class BaseControllerTest extends BaseSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .alwaysDo(print())
                .build();
    }

    protected MockMvc mockMvc() {
        return mockMvc;
    }

    protected String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected RequestPostProcessor authentication() {
        return new SecurePaymentTransactionProcessor();
    }


    private void authenticate() {
        CustomerDto authentication = CustomerDto.builder().id("Customer1").build();

        when(tokenAuthenticationService.getAuthentication(any(HttpServletRequest.class))).thenReturn(authentication);
        when(tokenAuthenticationService.supports(CustomerDto.class)).thenReturn(true);
        when(tokenAuthenticationService.authenticate(authentication)).thenReturn(authentication);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @After
    public void after() {
        verify(tokenAuthenticationService, atMost(1)).getAuthentication(any(HttpServletRequest.class));
        verify(tokenAuthenticationService, atMost(1)).supports(CustomerDto.class);
        verify(tokenAuthenticationService, atMost(1)).authenticate(any(CustomerDto.class));
        super.after();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    private class SecurePaymentTransactionProcessor implements RequestPostProcessor {

        private CustomerDto customerDto;

        @Override
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            authenticate();
            return request;
        }

    }
}
