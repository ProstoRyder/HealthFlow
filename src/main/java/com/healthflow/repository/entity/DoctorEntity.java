package com.healthflow.repository.entity;

import com.healthflow.domain.DoctorGender;
import com.healthflow.domain.DoctorQualification;
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
@Table(name = "doctors")
@Builder(toBuilder = true)
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(nullable = false, length = 100)
    String firstName;

    @Column(nullable = false, length = 100)
    String lastName;

    @Column(nullable = false, unique = true, length = 100)
    String email;

    @Column(nullable = false, length = 20)
    String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    DoctorGender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    DoctorQualification qualification;

    @ManyToOne(optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    SpecialtyEntity specialty;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    HospitalEntity hospital;

    @Builder.Default
    @OneToMany(mappedBy = "doctor")
    List<ReviewEntity> reviews = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "doctor")
    List<DoctorScheduleEntity> schedules = new ArrayList<>();
}
