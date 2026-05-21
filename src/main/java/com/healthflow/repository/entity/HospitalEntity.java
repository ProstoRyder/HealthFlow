package com.healthflow.repository.entity;

import com.healthflow.domain.HospitalType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hospitals")
@Builder(toBuilder = true)
public class HospitalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, unique = true, length = 150)
    String name;

    @Column(nullable = false, length = 255)
    String address;

    @Column(nullable = false, length = 20)
    String phoneNumber;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    HospitalType type;

    @Builder.Default
    @OneToMany(mappedBy = "hospital")
    List<DoctorEntity> doctors = new ArrayList<>();
}
