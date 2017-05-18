package com.sedatcan.service.impl;

import com.sedatcan.entity.Customer;
import com.sedatcan.exception.ToDoListErrorCode;
import com.sedatcan.exception.ToDoListException;
import com.sedatcan.model.CustomerDto;
import com.sedatcan.model.SignupRequest;
import com.sedatcan.model.SignupResponse;
import com.sedatcan.repository.CustomerRepository;
import com.sedatcan.security.TokenAuthenticationService;
import com.sedatcan.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Override
    public SignupResponse signup(SignupRequest signupRequest) {

        if (customerRepository.findByEmail(signupRequest.getEmail()) != null) {
            throw ToDoListException.builder().errorCode(ToDoListErrorCode.CUSTOMER_EXIST).httpStatus(HttpStatus.BAD_REQUEST).build();
        }
        String encryptedPassword = bCryptPasswordEncoder.encode(signupRequest.getPassword());

        Customer customer = Customer.builder()
                .name(signupRequest.getName())
                .surname(signupRequest.getSurname())
                .email(signupRequest.getEmail())
                .password(encryptedPassword)
                .id("Customer" + customerRepository.getCouchbaseOperations().getCouchbaseBucket().counter("idGeneratorForCustomer", 1, 0).content())
                .build();
        customer = customerRepository.save(customer);
        return SignupResponse.builder()
                .customerDto(CustomerDto.builder()
                        .id(customer.getId())
                        .name(customer.getName())
                        .surname(customer.getSurname())
                        .email(customer.getEmail())
                        .build())
                .token(tokenAuthenticationService.generateToken(customer))
                .build();
    }

    @Override
    public CustomerDto getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email);
        return customer == null ? null : CustomerDto.builder()
                .email(customer.getEmail())
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .password(customer.getPassword())
                .build();
    }

    @Override
    public CustomerDto getById(String id) {
        Customer customer = customerRepository.findOne(id);
        return customer == null ? null : CustomerDto.builder()
                .email(customer.getEmail())
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .password(customer.getPassword())
                .build();
    }

}