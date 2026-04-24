package com.healthflow.repository;

import com.healthflow.repository.entity.SpecialtyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, UUID> {
}
