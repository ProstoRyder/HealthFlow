package com.healthflow.dto.consultations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ConsultationRequestDto {

    @NotNull(message = "Consultation date and time is required.")
    LocalDateTime consultationDateTime;

    @NotBlank(message = "Symptoms are required.")
    @Size(max = 1000, message = "Symptoms must not exceed 1000 characters.")
    String symptoms;

    @NotBlank(message = "Diagnosis is required.")
    @Size(max = 1000, message = "Diagnosis must not exceed 1000 characters.")
    String diagnosis;

    @Size(max = 1000, message = "Recommendations must not exceed 1000 characters.")
    String recommendations;

    @NotNull(message = "Appointment id is required.")
    UUID appointmentId;
}
