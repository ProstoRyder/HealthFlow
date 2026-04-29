package com.healthflow.dto.appointments;

import com.healthflow.domain.AppointmentStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class AppointmentResponseDto {
    UUID id;
    LocalDateTime appointmentDateTime;
    AppointmentStatus status;
    String reason;
    UUID patientId;
    String patientFullName;
    UUID doctorId;
    String doctorFullName;
    String specialtyName;
}
