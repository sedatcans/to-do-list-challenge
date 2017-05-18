package com.sedatcan.service;

import com.sedatcan.model.CustomerDto;
import com.sedatcan.model.SignupRequest;
import com.sedatcan.model.SignupResponse;

public interface CustomerService {
    SignupResponse signup(SignupRequest signupRequest);

    CustomerDto getCustomerByEmail(String email);

    CustomerDto getById(String customerId);
}
