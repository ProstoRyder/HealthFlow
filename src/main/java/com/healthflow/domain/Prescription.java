package com.healthflow.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Prescription {
    UUID id;
    String medicineName;
    String dosage;
    String instructions;
    Integer durationDays;
    UUID consultationId;
    UUID patientId;
    String patientFullName;
    UUID doctorId;
    String doctorFullName;
}
