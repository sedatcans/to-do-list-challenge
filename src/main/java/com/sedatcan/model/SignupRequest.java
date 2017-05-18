package com.sedatcan.model;

import lombok.Data;

@Data
public class SignupRequest {

    private String password;

    private String name;

    private String surname;

    private String email;
}
