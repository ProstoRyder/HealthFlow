package com.healthflow.dto.consultations;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ConsultationResponseDto {
    UUID id;
    LocalDateTime consultationDateTime;
    String symptoms;
    String diagnosis;
    String recommendations;
    UUID appointmentId;
    UUID patientId;
    String patientFullName;
    UUID doctorId;
    String doctorFullName;
}
