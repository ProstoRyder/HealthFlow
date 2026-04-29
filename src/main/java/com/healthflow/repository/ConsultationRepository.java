package com.healthflow.repository;

import com.healthflow.repository.entity.ConsultationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsultationRepository extends JpaRepository<ConsultationEntity, UUID> {

    boolean existsByAppointmentId(UUID appointmentId);
}
