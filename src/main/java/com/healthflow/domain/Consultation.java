package com.healthflow.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Consultation {
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
