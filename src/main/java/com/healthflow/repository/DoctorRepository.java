package com.healthflow.repository;

import com.healthflow.repository.entity.DoctorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {
}
