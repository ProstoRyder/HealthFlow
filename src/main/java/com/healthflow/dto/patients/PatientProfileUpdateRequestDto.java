package com.healthflow.dto.patients;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class PatientProfileUpdateRequestDto {

    @Size(max = 20, message = "Phone number must not exceed 20 characters.")
    String phoneNumber;

    @Past(message = "Date of birth must be in the past.")
    LocalDate dateOfBirth;
}
