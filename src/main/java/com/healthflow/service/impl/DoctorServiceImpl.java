package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Doctor;
import com.healthflow.dto.doctors.DoctorRequestDto;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.SpecialtyRepository;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.SpecialtyEntity;
import com.healthflow.service.DoctorService;
import com.healthflow.service.mapper.DoctorMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final DoctorMapper doctorMapper;

    @Override
    @Transactional
    public Doctor create(DoctorRequestDto requestDto) {
        SpecialtyEntity specialtyEntity = findSpecialtyById(requestDto.getSpecialtyId());
        DoctorEntity doctorEntity = doctorMapper.toEntity(requestDto);
        doctorEntity.setSpecialty(specialtyEntity);

        return doctorMapper.toDoctor(doctorRepository.save(doctorEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> getAll() {
        return doctorMapper.toDoctorList(doctorRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Doctor getById(UUID id) {
        return doctorMapper.toDoctor(findDoctorById(id));
    }

    @Override
    @Transactional
    public Doctor update(UUID id, DoctorRequestDto requestDto) {
        DoctorEntity doctorEntity = findDoctorById(id);
        SpecialtyEntity specialtyEntity = findSpecialtyById(requestDto.getSpecialtyId());

        doctorMapper.updateEntityFromDto(requestDto, doctorEntity);
        doctorEntity.setSpecialty(specialtyEntity);

        return doctorMapper.toDoctor(doctorRepository.save(doctorEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        doctorRepository.delete(findDoctorById(id));
    }

    private DoctorEntity findDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + id + " not found"));
    }

    private SpecialtyEntity findSpecialtyById(UUID id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty with id " + id + " not found"));
    }
}
