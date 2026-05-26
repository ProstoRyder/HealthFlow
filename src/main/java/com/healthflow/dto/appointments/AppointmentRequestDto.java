package com.healthflow.dto.appointments;

import com.healthflow.domain.AppointmentStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class AppointmentRequestDto {

    @NotNull(message = "Appointment date and time is required.")
    @FutureOrPresent(message = "Appointment date and time must be in the present or future.")
    LocalDateTime appointmentDateTime;

    AppointmentStatus status;

    @Size(max = 500, message = "Reason must not exceed 500 characters.")
    String reason;

    UUID patientId;

    @NotNull(message = "Doctor id is required.")
    UUID doctorId;
}
