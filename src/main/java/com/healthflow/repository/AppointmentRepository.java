package com.healthflow.repository;

import com.healthflow.repository.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.healthflow.domain.AppointmentStatus;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    List<AppointmentEntity> findByPatient_Id(UUID patientId);

    List<AppointmentEntity> findByDoctor_Id(UUID doctorId);

    boolean existsByDoctor_IdAndAppointmentDateTime(UUID doctorId, LocalDateTime appointmentDateTime);

    boolean existsByDoctor_IdAndAppointmentDateTimeAndIdNot(
            UUID doctorId,
            LocalDateTime appointmentDateTime,
            UUID id
    );

    boolean existsByDoctor_IdAndAppointmentDateTimeAndStatusIn(
            UUID doctorId,
            LocalDateTime appointmentDateTime,
            List<AppointmentStatus> statuses
    );

    boolean existsByDoctor_IdAndAppointmentDateTimeAndIdNotAndStatusIn(
            UUID doctorId,
            LocalDateTime appointmentDateTime,
            UUID id,
            List<AppointmentStatus> statuses
    );

    boolean existsByPatient_IdAndDoctor_IdAndStatus(
            UUID patientId,
            UUID doctorId,
            com.healthflow.domain.AppointmentStatus status
    );
}
