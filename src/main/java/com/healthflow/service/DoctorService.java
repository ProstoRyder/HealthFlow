package com.healthflow.service;

import com.healthflow.domain.Doctor;
import com.healthflow.dto.doctors.DoctorRequestDto;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    Doctor create(DoctorRequestDto requestDto);

    List<Doctor> getAll();

    Doctor getById(UUID id);

    Doctor update(UUID id, DoctorRequestDto requestDto);

    void delete(UUID id);
}
