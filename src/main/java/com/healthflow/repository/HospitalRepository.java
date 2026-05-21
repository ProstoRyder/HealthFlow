package com.healthflow.repository;

import com.healthflow.repository.entity.HospitalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HospitalRepository extends JpaRepository<HospitalEntity, UUID> {
}
