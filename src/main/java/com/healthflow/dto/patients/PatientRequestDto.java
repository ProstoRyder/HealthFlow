package com.healthflow.dto.patients;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class PatientRequestDto {

    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name must not exceed 100 characters.")
    String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name must not exceed 100 characters.")
    String lastName;

    @Size(max = 100, message = "Patronymic must not exceed 100 characters.")
    String patronymic;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(max = 100, message = "Email must not exceed 100 characters.")
    String email;

    @NotBlank(message = "Phone number is required.")
    @Size(max = 20, message = "Phone number must not exceed 20 characters.")
    String phoneNumber;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be in the past.")
    LocalDate dateOfBirth;
}
