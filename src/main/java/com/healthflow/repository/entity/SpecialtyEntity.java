package com.healthflow.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "specialties")
@Builder(toBuilder = true)
public class SpecialtyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, unique = true, length = 100)
    String name;

    @Column(length = 255)
    String description;

    @Builder.Default
    @OneToMany(mappedBy = "specialty")
    List<DoctorEntity> doctors = new ArrayList<>();
}
