package com.healthflow.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Doctor {
    UUID id;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    DoctorGender gender;
    DoctorQualification qualification;
    Specialty specialty;
    Hospital hospital;
}
