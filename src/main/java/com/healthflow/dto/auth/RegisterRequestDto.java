package com.healthflow.dto.auth;

import com.healthflow.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class RegisterRequestDto {

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(max = 100, message = "Email must not exceed 100 characters.")
    String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters.")
    String password;

    UserRole role;

    @Size(max = 100, message = "First name must not exceed 100 characters.")
    String firstName;

    @Size(max = 100, message = "Last name must not exceed 100 characters.")
    String lastName;

    @Size(max = 100, message = "Patronymic must not exceed 100 characters.")
    String patronymic;

    UUID doctorId;
}
