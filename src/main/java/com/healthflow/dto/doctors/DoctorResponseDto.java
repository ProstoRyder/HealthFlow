package com.healthflow.dto.doctors;

import com.healthflow.domain.DoctorGender;
import com.healthflow.domain.DoctorQualification;
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
    DoctorGender gender;
    DoctorQualification qualification;
    UUID specialtyId;
    String specialtyName;
    UUID hospitalId;
    String hospitalName;
}
