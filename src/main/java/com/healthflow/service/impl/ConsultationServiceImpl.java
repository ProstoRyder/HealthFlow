package com.healthflow.service.impl;

import com.healthflow.common.BadRequestException;
import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.AppointmentStatus;
import com.healthflow.domain.Consultation;
import com.healthflow.dto.consultations.ConsultationRequestDto;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.ConsultationRepository;
import com.healthflow.repository.entity.AppointmentEntity;
import com.healthflow.repository.entity.ConsultationEntity;
import com.healthflow.service.ConsultationService;
import com.healthflow.service.mapper.ConsultationMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;
    private final ConsultationMapper consultationMapper;

    @Override
    @Transactional
    public Consultation create(ConsultationRequestDto requestDto) {
        AppointmentEntity appointmentEntity = findAppointmentById(requestDto.getAppointmentId());

        if (consultationRepository.existsByAppointmentId(requestDto.getAppointmentId())) {
            throw new BadRequestException("Consultation for appointment with id " + requestDto.getAppointmentId() + " already exists");
        }

        ConsultationEntity consultationEntity = consultationMapper.toEntity(requestDto);
        consultationEntity.setAppointment(appointmentEntity);
        appointmentEntity.setStatus(AppointmentStatus.COMPLETED);

        return consultationMapper.toConsultation(consultationRepository.save(consultationEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Consultation> getAll() {
        return consultationMapper.toConsultationList(consultationRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Consultation getById(UUID id) {
        return consultationMapper.toConsultation(findConsultationById(id));
    }

    @Override
    @Transactional
    public Consultation update(UUID id, ConsultationRequestDto requestDto) {
        ConsultationEntity consultationEntity = findConsultationById(id);
        AppointmentEntity appointmentEntity = findAppointmentById(requestDto.getAppointmentId());

        if (!consultationEntity.getAppointment().getId().equals(requestDto.getAppointmentId())
                && consultationRepository.existsByAppointmentId(requestDto.getAppointmentId())) {
            throw new BadRequestException("Consultation for appointment with id " + requestDto.getAppointmentId() + " already exists");
        }

        consultationMapper.updateEntityFromDto(requestDto, consultationEntity);
        consultationEntity.setAppointment(appointmentEntity);
        appointmentEntity.setStatus(AppointmentStatus.COMPLETED);

        return consultationMapper.toConsultation(consultationRepository.save(consultationEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        consultationRepository.delete(findConsultationById(id));
    }

    private ConsultationEntity findConsultationById(UUID id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation with id " + id + " not found"));
    }

    private AppointmentEntity findAppointmentById(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id " + id + " not found"));
    }
}
