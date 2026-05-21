package com.healthflow.service;

import com.healthflow.domain.Hospital;
import com.healthflow.dto.hospitals.HospitalRequestDto;

import java.util.List;
import java.util.UUID;

public interface HospitalService {

    Hospital create(HospitalRequestDto requestDto);

    List<Hospital> getAll();

    Hospital getById(UUID id);

    Hospital update(UUID id, HospitalRequestDto requestDto);

    void delete(UUID id);
}
