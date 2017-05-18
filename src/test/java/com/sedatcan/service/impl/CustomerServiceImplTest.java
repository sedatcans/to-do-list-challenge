package com.sedatcan.service.impl;

import com.couchbase.client.java.CouchbaseBucket;
import com.couchbase.client.java.document.JsonLongDocument;
import com.sedatcan.common.BaseServiceTest;
import com.sedatcan.entity.Customer;
import com.sedatcan.exception.ToDoListException;
import com.sedatcan.model.CustomerDto;
import com.sedatcan.model.SignupRequest;
import com.sedatcan.model.SignupResponse;
import com.sedatcan.repository.CustomerRepository;
import com.sedatcan.security.TokenAuthenticationService;
import com.sedatcan.service.CustomerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.couchbase.core.CouchbaseOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest extends BaseServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void shouldSignupSuccess() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().email("sedatcan@gmail.com").name("sedatcan").password("password").surname("sonat").build();
        SignupResponse signupResponseExpected = SignupResponse.builder()
                .token("I am Token").customerDto(CustomerDto.builder()
                        .email("sedatcan@gmail.com")
                        .name("sedatcan")
                        .password("password")
                        .surname("sonat")
                        .id("Customer1").build()).build();
        when(customerRepository.findByEmail(signupRequest.getEmail())).thenReturn(null);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(Customer.builder()
                .email("sedatcan@gmail.com")
                .name("sedatcan")
                .password("password")
                .surname("sonat")
                .id("Customer1").build());
        when(tokenAuthenticationService.generateToken(Customer.builder()
                .email("sedatcan@gmail.com")
                .name("sedatcan")
                .password("password")
                .surname("sonat")
                .id("Customer1").build())).thenReturn("I am token");
        CouchbaseOperations couchbaseOperations = mock(CouchbaseOperations.class);
        CouchbaseBucket couchbaseBucket = mock(CouchbaseBucket.class);
        JsonLongDocument jsonLong = mock(JsonLongDocument.class);
        when(customerRepository.getCouchbaseOperations()).thenReturn(couchbaseOperations);
        when(couchbaseOperations.getCouchbaseBucket()).thenReturn(couchbaseBucket);
        when(couchbaseBucket.counter("idGeneratorForCustomer", 1, 0)).thenReturn(jsonLong);
        when(jsonLong.content()).thenReturn(1L);
        SignupResponse signupResponse = customerService.signup(signupRequest);

        assertThat(signupResponse.getToken(), equalTo("I am token"));
        assertThat(signupResponse.getCustomerDto().getId(), equalTo("Customer1"));
        verify(customerRepository).findByEmail("sedatcan@gmail.com");
        verify(bCryptPasswordEncoder).encode("password");
        verify(customerRepository).getCouchbaseOperations();
        verify(couchbaseOperations).getCouchbaseBucket();
        verify(couchbaseBucket).counter("idGeneratorForCustomer", 1, 0);
        verify(jsonLong).content();
        verify(customerRepository).save(Customer.builder()
                .email("sedatcan@gmail.com")
                .name("sedatcan")
                .password("encodedPassword")
                .surname("sonat")
                .id("Customer1").build());
        verify(tokenAuthenticationService).generateToken(any(Customer.class));
    }

    @Test(expected = ToDoListException.class)
    public void shouldSignupAllreadyRegistered() throws Exception {
        SignupRequest signupRequest = SignupRequest.builder().email("sedatcan@gmail.com").name("sedatcan").password("password").surname("sonat").build();
        when(customerRepository.findByEmail("sedatcan@gmail.com")).thenReturn(Customer.builder().build());

        try {
            SignupResponse signupResponse = customerService.signup(signupRequest);
        } catch (Exception e) {
            throw e;
        } finally {
            verify(customerRepository).findByEmail("sedatcan@gmail.com");

        }
    }

    @Test
    public void shouldGetCustomerByEmail() throws Exception {
        when(customerRepository.findByEmail("sedatcan@gmail.com")).thenReturn(Customer.builder().id("id").build());

        CustomerDto customerDto = customerService.getCustomerByEmail("sedatcan@gmail.com");

        assertThat(customerDto.getId(), equalTo("id"));
        verify(customerRepository).findByEmail("sedatcan@gmail.com");

    }

    @Test
    public void shouldGetCustomerByEmailNotFound() throws Exception {
        when(customerRepository.findByEmail("sedatcan@gmail.com")).thenReturn(null);

        CustomerDto customerDto = customerService.getCustomerByEmail("sedatcan@gmail.com");

        assertThat(customerDto, nullValue());
        verify(customerRepository).findByEmail("sedatcan@gmail.com");
    }

    @Test
    public void shouldGetById() throws Exception {
        when(customerRepository.findOne("id")).thenReturn(Customer.builder().id("id").name("name").build());

        CustomerDto customerDto = customerService.getById("id");

        assertThat(customerDto.getName(), equalTo("name"));
        verify(customerRepository).findOne("id");
    }
}