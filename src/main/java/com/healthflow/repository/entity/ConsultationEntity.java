package com.healthflow.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultations")
@Builder(toBuilder = true)
public class ConsultationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false)
    LocalDateTime consultationDateTime;

    @Column(nullable = false, length = 1000)
    String symptoms;

    @Column(nullable = false, length = 1000)
    String diagnosis;

    @Column(length = 1000)
    String recommendations;

    @OneToOne(optional = false)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    AppointmentEntity appointment;

    @Builder.Default
    @OneToMany(mappedBy = "consultation")
    List<PrescriptionEntity> prescriptions = new ArrayList<>();
}
