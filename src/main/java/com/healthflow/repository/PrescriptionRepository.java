package com.healthflow.repository;

import com.healthflow.repository.entity.PrescriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<PrescriptionEntity, UUID> {

    List<PrescriptionEntity> findByConsultation_Appointment_Patient_Id(UUID patientId);

    List<PrescriptionEntity> findByConsultation_Appointment_Doctor_Id(UUID doctorId);

    List<PrescriptionEntity> findByConsultation_Id(UUID consultationId);
}
