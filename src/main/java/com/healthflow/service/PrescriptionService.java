package com.healthflow.service;

import com.healthflow.domain.Prescription;
import com.healthflow.dto.prescriptions.PrescriptionRequestDto;

import java.util.List;
import java.util.UUID;

public interface PrescriptionService {

    Prescription create(PrescriptionRequestDto requestDto);

    List<Prescription> getAll();

    Prescription getById(UUID id);

    Prescription update(UUID id, PrescriptionRequestDto requestDto);

    void delete(UUID id);
}
