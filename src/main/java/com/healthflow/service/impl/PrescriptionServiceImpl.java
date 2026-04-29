package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Prescription;
import com.healthflow.dto.prescriptions.PrescriptionRequestDto;
import com.healthflow.repository.ConsultationRepository;
import com.healthflow.repository.PrescriptionRepository;
import com.healthflow.repository.entity.ConsultationEntity;
import com.healthflow.repository.entity.PrescriptionEntity;
import com.healthflow.service.PrescriptionService;
import com.healthflow.service.mapper.PrescriptionMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsultationRepository consultationRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Override
    @Transactional
    public Prescription create(PrescriptionRequestDto requestDto) {
        ConsultationEntity consultationEntity = findConsultationById(requestDto.getConsultationId());

        PrescriptionEntity prescriptionEntity = prescriptionMapper.toEntity(requestDto);
        prescriptionEntity.setConsultation(consultationEntity);

        return prescriptionMapper.toPrescription(prescriptionRepository.save(prescriptionEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Prescription> getAll() {
        return prescriptionMapper.toPrescriptionList(prescriptionRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Prescription getById(UUID id) {
        return prescriptionMapper.toPrescription(findPrescriptionById(id));
    }

    @Override
    @Transactional
    public Prescription update(UUID id, PrescriptionRequestDto requestDto) {
        PrescriptionEntity prescriptionEntity = findPrescriptionById(id);
        ConsultationEntity consultationEntity = findConsultationById(requestDto.getConsultationId());

        prescriptionMapper.updateEntityFromDto(requestDto, prescriptionEntity);
        prescriptionEntity.setConsultation(consultationEntity);

        return prescriptionMapper.toPrescription(prescriptionRepository.save(prescriptionEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        prescriptionRepository.delete(findPrescriptionById(id));
    }

    private PrescriptionEntity findPrescriptionById(UUID id) {
        return prescriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescription with id " + id + " not found"));
    }

    private ConsultationEntity findConsultationById(UUID id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation with id " + id + " not found"));
    }
}
