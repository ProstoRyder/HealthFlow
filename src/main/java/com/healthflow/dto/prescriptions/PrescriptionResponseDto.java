package com.healthflow.dto.prescriptions;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PrescriptionResponseDto {
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
