package com.healthflow.service;

import com.healthflow.domain.Patient;
import com.healthflow.dto.patients.PatientRequestDto;

import java.util.List;
import java.util.UUID;

public interface PatientService {

    Patient create(PatientRequestDto requestDto);

    List<Patient> getAll();

    Patient getById(UUID id);

    Patient update(UUID id, PatientRequestDto requestDto);

    void delete(UUID id);
}
