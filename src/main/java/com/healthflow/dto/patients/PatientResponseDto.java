package com.healthflow.dto.patients;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.UUID;

@Value
@Builder
public class PatientResponseDto {
    UUID id;
    String firstName;
    String lastName;
    String patronymic;
    String email;
    String avatarUrl;
    String phoneNumber;
    LocalDate dateOfBirth;
}
