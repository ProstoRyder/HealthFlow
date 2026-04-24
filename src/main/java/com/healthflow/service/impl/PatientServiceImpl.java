package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Patient;
import com.healthflow.dto.patients.PatientRequestDto;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.entity.PatientEntity;
import com.healthflow.service.PatientService;
import com.healthflow.service.mapper.PatientMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    @Transactional
    public Patient create(PatientRequestDto requestDto) {
        PatientEntity patientEntity = patientMapper.toEntity(requestDto);
        return patientMapper.toPatient(patientRepository.save(patientEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Patient> getAll() {
        return patientMapper.toPatientList(patientRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Patient getById(UUID id) {
        return patientMapper.toPatient(findPatientById(id));
    }

    @Override
    @Transactional
    public Patient update(UUID id, PatientRequestDto requestDto) {
        PatientEntity patientEntity = findPatientById(id);
        patientMapper.updateEntityFromDto(requestDto, patientEntity);
        return patientMapper.toPatient(patientRepository.save(patientEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        patientRepository.delete(findPatientById(id));
    }

    private PatientEntity findPatientById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + id + " not found"));
    }
}
