package com.healthflow.dto.appointments;

import com.healthflow.domain.AppointmentStatus;
import jakarta.validation.constraints.NotNull;

public record AppointmentStatusUpdateRequestDto(
        @NotNull(message = "Status is required.")
        AppointmentStatus status
) {
}
