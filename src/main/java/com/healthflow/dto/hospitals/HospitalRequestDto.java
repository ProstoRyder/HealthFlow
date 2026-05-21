package com.healthflow.dto.hospitals;

import com.healthflow.domain.HospitalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class HospitalRequestDto {

    @NotBlank(message = "Name is required.")
    @Size(max = 150, message = "Name must not exceed 150 characters.")
    String name;

    @NotBlank(message = "Address is required.")
    @Size(max = 255, message = "Address must not exceed 255 characters.")
    String address;

    @NotBlank(message = "Phone number is required.")
    @Size(max = 20, message = "Phone number must not exceed 20 characters.")
    String phoneNumber;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be valid.")
    @Size(max = 100, message = "Email must not exceed 100 characters.")
    String email;

    @NotNull(message = "Hospital type is required.")
    HospitalType type;
}
