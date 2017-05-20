package com.sedatcan.controller;

import com.sedatcan.model.SignupRequest;
import com.sedatcan.model.SignupResponse;
import com.sedatcan.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/customers")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest signupRequest) {
        return new ResponseEntity<SignupResponse>(customerService.signup(signupRequest), HttpStatus.ACCEPTED);
    }

}
