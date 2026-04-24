package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Specialty;
import com.healthflow.dto.specialties.SpecialtyRequestDto;
import com.healthflow.repository.SpecialtyRepository;
import com.healthflow.repository.entity.SpecialtyEntity;
import com.healthflow.service.SpecialtyService;
import com.healthflow.service.mapper.SpecialtyMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    @Transactional
    public Specialty create(SpecialtyRequestDto requestDto) {
        SpecialtyEntity specialtyEntity = specialtyMapper.toEntity(requestDto);
        return specialtyMapper.toSpecialty(specialtyRepository.save(specialtyEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Specialty> getAll() {
        return specialtyMapper.toSpecialtyList(specialtyRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Specialty getById(UUID id) {
        return specialtyMapper.toSpecialty(findSpecialtyById(id));
    }

    @Override
    @Transactional
    public Specialty update(UUID id, SpecialtyRequestDto requestDto) {
        SpecialtyEntity specialtyEntity = findSpecialtyById(id);
        specialtyMapper.updateEntityFromDto(requestDto, specialtyEntity);
        return specialtyMapper.toSpecialty(specialtyRepository.save(specialtyEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        specialtyRepository.delete(findSpecialtyById(id));
    }

    private SpecialtyEntity findSpecialtyById(UUID id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty with id " + id + " not found"));
    }
}
