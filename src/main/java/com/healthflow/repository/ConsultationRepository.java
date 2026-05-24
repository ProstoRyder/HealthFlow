package com.healthflow.repository;

import com.healthflow.repository.entity.ConsultationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<ConsultationEntity, UUID> {

    boolean existsByAppointmentId(UUID appointmentId);

    boolean existsByIdAndAppointment_Patient_Id(UUID consultationId, UUID patientId);

    boolean existsByIdAndAppointment_Doctor_Id(UUID consultationId, UUID doctorId);

    List<ConsultationEntity> findByAppointment_Patient_Id(UUID patientId);

    List<ConsultationEntity> findByAppointment_Doctor_Id(UUID doctorId);
}
