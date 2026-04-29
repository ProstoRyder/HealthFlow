package com.healthflow.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "prescriptions")
@Builder(toBuilder = true)
public class PrescriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, length = 150)
    String medicineName;

    @Column(nullable = false, length = 100)
    String dosage;

    @Column(nullable = false, length = 1000)
    String instructions;

    @Column(nullable = false)
    Integer durationDays;

    @ManyToOne(optional = false)
    @JoinColumn(name = "consultation_id", nullable = false)
    ConsultationEntity consultation;
}
