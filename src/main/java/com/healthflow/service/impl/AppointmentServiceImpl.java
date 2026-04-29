package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Appointment;
import com.healthflow.dto.appointments.AppointmentRequestDto;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.entity.AppointmentEntity;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.PatientEntity;
import com.healthflow.service.AppointmentService;
import com.healthflow.service.mapper.AppointmentMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public Appointment create(AppointmentRequestDto requestDto) {
        PatientEntity patientEntity = findPatientById(requestDto.getPatientId());
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        AppointmentEntity appointmentEntity = appointmentMapper.toEntity(requestDto);
        appointmentEntity.setPatient(patientEntity);
        appointmentEntity.setDoctor(doctorEntity);

        return appointmentMapper.toAppointment(appointmentRepository.save(appointmentEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAll() {
        return appointmentMapper.toAppointmentList(appointmentRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Appointment getById(UUID id) {
        return appointmentMapper.toAppointment(findAppointmentById(id));
    }

    @Override
    @Transactional
    public Appointment update(UUID id, AppointmentRequestDto requestDto) {
        AppointmentEntity appointmentEntity = findAppointmentById(id);
        PatientEntity patientEntity = findPatientById(requestDto.getPatientId());
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        appointmentMapper.updateEntityFromDto(requestDto, appointmentEntity);
        appointmentEntity.setPatient(patientEntity);
        appointmentEntity.setDoctor(doctorEntity);

        return appointmentMapper.toAppointment(appointmentRepository.save(appointmentEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        appointmentRepository.delete(findAppointmentById(id));
    }

    private AppointmentEntity findAppointmentById(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id " + id + " not found"));
    }

    private PatientEntity findPatientById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + id + " not found"));
    }

    private DoctorEntity findDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + id + " not found"));
    }
}
