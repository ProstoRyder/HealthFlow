package com.healthflow.dto.doctors;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class DoctorResponseDto {
    UUID id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    UUID specialtyId;
    String specialtyName;
}
