package com.healthflow.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "patients")
@Builder(toBuilder = true)
public class PatientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, length = 100)
    String firstName;

    @Column(nullable = false, length = 100)
    String lastName;

    @Column(length = 100)
    String patronymic;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(length = 500)
    String avatarUrl;

    @Column(length = 500)
    String avatarKey;

    @Column(length = 20)
    String phoneNumber;

    @Column
    LocalDate dateOfBirth;

    @Builder.Default
    @OneToMany(mappedBy = "patient")
    List<ReviewEntity> reviews = new ArrayList<>();
}
