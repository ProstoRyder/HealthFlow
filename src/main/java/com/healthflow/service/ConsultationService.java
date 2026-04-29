package com.healthflow.service;

import com.healthflow.domain.Consultation;
import com.healthflow.dto.consultations.ConsultationRequestDto;

import java.util.List;
import java.util.UUID;

public interface ConsultationService {

    Consultation create(ConsultationRequestDto requestDto);

    List<Consultation> getAll();

    Consultation getById(UUID id);

    Consultation update(UUID id, ConsultationRequestDto requestDto);

    void delete(UUID id);
}
