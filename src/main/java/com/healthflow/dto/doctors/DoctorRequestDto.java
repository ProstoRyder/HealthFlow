package com.healthflow.dto.doctors;

import com.healthflow.domain.DoctorGender;
import com.healthflow.domain.DoctorQualification;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class DoctorRequestDto {
    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name must not exceed 100 characters.")
    String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name must not exceed 100 characters.")
    String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(max = 100, message = "Email must not exceed 100 characters.")
    String email;

    @NotBlank(message = "Phone number is required.")
    @Size(max = 20, message = "Phone number must not exceed 20 characters.")
    String phoneNumber;

    @NotNull(message = "Gender is required.")
    DoctorGender gender;

    @NotNull(message = "Qualification is required.")
    DoctorQualification qualification;

    @NotNull(message = "Specialty id is required.")
    UUID specialtyId;

    @NotNull(message = "Hospital id is required.")
    UUID hospitalId;
}
