package com.sedatcan.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    private String password;

    private String name;

    private String surname;

    @NotBlank
    @Email
    private String email;
}
