package com.healthflow.service;

import com.healthflow.domain.Specialty;
import com.healthflow.dto.specialties.SpecialtyRequestDto;

import java.util.List;
import java.util.UUID;

public interface SpecialtyService {

    Specialty create(SpecialtyRequestDto requestDto);

    List<Specialty> getAll();

    Specialty getById(UUID id);

    Specialty update(UUID id, SpecialtyRequestDto requestDto);

    void delete(UUID id);
}
