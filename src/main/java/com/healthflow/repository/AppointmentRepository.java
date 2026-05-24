package com.healthflow.repository;

import com.healthflow.repository.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    List<AppointmentEntity> findByPatient_Id(UUID patientId);

    List<AppointmentEntity> findByDoctor_Id(UUID doctorId);

    boolean existsByDoctor_IdAndAppointmentDateTime(UUID doctorId, LocalDateTime appointmentDateTime);

    boolean existsByDoctor_IdAndAppointmentDateTimeAndIdNot(
            UUID doctorId,
            LocalDateTime appointmentDateTime,
            UUID id
    );
}
