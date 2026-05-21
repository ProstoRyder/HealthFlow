package com.healthflow.dto.doctorSchedules;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Value
@Builder
public class DoctorScheduleRequestDto {

    @NotNull(message = "Day of week is required.")
    DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required.")
    LocalTime startTime;

    @NotNull(message = "End time is required.")
    LocalTime endTime;

    @NotNull(message = "Doctor id is required.")
    UUID doctorId;
}
