package com.healthflow.domain;

import lombok.Builder;
import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class DoctorSchedule {
    UUID id;
    DayOfWeek dayOfWeek;
    LocalTime startTime;
    LocalTime endTime;
    UUID doctorId;
    String doctorFullName;
}
