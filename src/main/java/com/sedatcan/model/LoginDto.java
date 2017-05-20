package com.sedatcan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@AllArgsConstructor
@Data
public class LoginDto {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
