package com.sedatcan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SignupResponse {

    private CustomerDto customerDto;

    private String token;
}
