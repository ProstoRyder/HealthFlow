package com.healthflow.service;

import com.healthflow.domain.DoctorSchedule;
import com.healthflow.dto.doctorSchedules.DoctorScheduleRequestDto;

import java.util.List;
import java.util.UUID;

public interface DoctorScheduleService {

    DoctorSchedule create(DoctorScheduleRequestDto requestDto);

    List<DoctorSchedule> getAll();

    DoctorSchedule getById(UUID id);

    DoctorSchedule update(UUID id, DoctorScheduleRequestDto requestDto);

    void delete(UUID id);
}
