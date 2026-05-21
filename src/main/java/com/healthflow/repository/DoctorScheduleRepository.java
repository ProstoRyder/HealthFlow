package com.healthflow.repository;

import com.healthflow.repository.entity.DoctorScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface DoctorScheduleRepository extends JpaRepository<DoctorScheduleEntity, UUID> {

    List<DoctorScheduleEntity> findByDoctorIdAndDayOfWeek(UUID doctorId, DayOfWeek dayOfWeek);
}
