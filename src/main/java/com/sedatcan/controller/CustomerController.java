package com.sedatcan.controller;

import com.sedatcan.model.SignupRequest;
import com.sedatcan.model.SignupResponse;
import com.sedatcan.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        return new ResponseEntity<SignupResponse>(customerService.signup(signupRequest), HttpStatus.ACCEPTED);
    }

}
