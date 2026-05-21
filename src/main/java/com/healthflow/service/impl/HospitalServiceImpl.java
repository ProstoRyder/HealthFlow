package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Hospital;
import com.healthflow.dto.hospitals.HospitalRequestDto;
import com.healthflow.repository.HospitalRepository;
import com.healthflow.repository.entity.HospitalEntity;
import com.healthflow.service.HospitalService;
import com.healthflow.service.mapper.HospitalMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    @Override
    @Transactional
    public Hospital create(HospitalRequestDto requestDto) {
        HospitalEntity hospitalEntity = hospitalMapper.toEntity(requestDto);
        return hospitalMapper.toHospital(hospitalRepository.save(hospitalEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Hospital> getAll() {
        return hospitalMapper.toHospitalList(hospitalRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Hospital getById(UUID id) {
        return hospitalMapper.toHospital(findHospitalById(id));
    }

    @Override
    @Transactional
    public Hospital update(UUID id, HospitalRequestDto requestDto) {
        HospitalEntity hospitalEntity = findHospitalById(id);
        hospitalMapper.updateEntityFromDto(requestDto, hospitalEntity);
        return hospitalMapper.toHospital(hospitalRepository.save(hospitalEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        hospitalRepository.delete(findHospitalById(id));
    }

    private HospitalEntity findHospitalById(UUID id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital with id " + id + " not found"));
    }
}
