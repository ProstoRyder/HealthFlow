package com.healthflow.dto.doctorSchedules;

import lombok.Builder;
import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Value
@Builder
public class DoctorScheduleResponseDto {
    UUID id;
    DayOfWeek dayOfWeek;
    LocalTime startTime;
    LocalTime endTime;
    UUID doctorId;
    String doctorFullName;
}
