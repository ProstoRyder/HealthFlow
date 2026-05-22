package com.healthflow.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginRequestDto {

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    String email;

    @NotBlank(message = "Password is required.")
    String password;
}
