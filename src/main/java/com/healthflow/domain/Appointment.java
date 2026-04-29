package com.healthflow.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Appointment {
    UUID id;
    LocalDateTime appointmentDateTime;
    AppointmentStatus status;
    String reason;
    UUID patientId;
    String patientFirstName;
    String patientLastName;
    UUID doctorId;
    String doctorFirstName;
    String doctorLastName;
    String specialtyName;
}
