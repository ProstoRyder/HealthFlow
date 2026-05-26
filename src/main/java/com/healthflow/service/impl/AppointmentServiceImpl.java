package com.healthflow.service.impl;

import com.healthflow.common.BadRequestException;
import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Appointment;
import com.healthflow.domain.AppointmentStatus;
import com.healthflow.domain.UserRole;
import com.healthflow.dto.appointments.AppointmentRequestDto;
import com.healthflow.dto.appointments.AppointmentStatusUpdateRequestDto;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.DoctorScheduleRepository;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.UserRepository;
import com.healthflow.repository.entity.AppointmentEntity;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.DoctorScheduleEntity;
import com.healthflow.repository.entity.PatientEntity;
import com.healthflow.repository.entity.UserEntity;
import com.healthflow.service.AppointmentService;
import com.healthflow.service.mapper.AppointmentMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    @Transactional
    public Appointment create(AppointmentRequestDto requestDto) {
        if (requestDto.getPatientId() == null) {
            throw new BadRequestException("Patient id is required.");
        }
        if (requestDto.getStatus() == null) {
            throw new BadRequestException("Status is required.");
        }

        PatientEntity patientEntity = findPatientById(requestDto.getPatientId());
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());
        validateDoctorSchedule(requestDto);
        validateDoctorAppointmentConflict(requestDto);

        AppointmentEntity appointmentEntity = appointmentMapper.toEntity(requestDto);
        appointmentEntity.setPatient(patientEntity);
        appointmentEntity.setDoctor(doctorEntity);

        return appointmentMapper.toAppointment(appointmentRepository.save(appointmentEntity));
    }

    @Override
    @Transactional
    public Appointment createByCurrentUser(AppointmentRequestDto requestDto, String currentUserEmail) {
        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new BadRequestException("Current user was not found"));

        if (user.getRole() == UserRole.ADMIN) {
            return create(requestDto);
        }

        if (user.getRole() != UserRole.PATIENT) {
            throw new BadRequestException("Only patient can create appointment");
        }

        if (user.getPatient() == null) {
            throw new BadRequestException("Current user is not linked to a patient");
        }
        if (requestDto.getDoctorId() == null) {
            throw new BadRequestException("Doctor id is required.");
        }
        if (requestDto.getAppointmentDateTime() == null) {
            throw new BadRequestException("Appointment date and time is required.");
        }

        PatientEntity patientEntity = user.getPatient();
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        AppointmentRequestDto normalizedRequest = AppointmentRequestDto.builder()
                .appointmentDateTime(requestDto.getAppointmentDateTime())
                .status(AppointmentStatus.SCHEDULED)
                .reason(requestDto.getReason())
                .patientId(patientEntity.getId())
                .doctorId(requestDto.getDoctorId())
                .build();

        validateDoctorSchedule(normalizedRequest);
        validateDoctorAppointmentConflict(normalizedRequest);

        AppointmentEntity appointmentEntity = appointmentMapper.toEntity(normalizedRequest);
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
        validateDoctorSchedule(requestDto);
        validateDoctorAppointmentConflict(id, requestDto);

        appointmentMapper.updateEntityFromDto(requestDto, appointmentEntity);
        appointmentEntity.setPatient(patientEntity);
        appointmentEntity.setDoctor(doctorEntity);

        return appointmentMapper.toAppointment(appointmentRepository.save(appointmentEntity));
    }

    @Override
    @Transactional
    public Appointment updateStatus(UUID id, AppointmentStatusUpdateRequestDto requestDto) {
        AppointmentEntity appointmentEntity = findAppointmentById(id);
        appointmentEntity.setStatus(requestDto.status());
        return appointmentMapper.toAppointment(appointmentRepository.save(appointmentEntity));
    }

    @Override
    @Transactional
    public Appointment updateStatusByCurrentUser(
            UUID id,
            AppointmentStatusUpdateRequestDto requestDto,
            String currentUserEmail
    ) {
        AppointmentEntity appointmentEntity = findAppointmentById(id);
        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new BadRequestException("Current user was not found"));

        AppointmentStatus requestedStatus = requestDto.status();

        if (user.getRole() == UserRole.ADMIN) {
            appointmentEntity.setStatus(requestedStatus);
            return appointmentMapper.toAppointment(appointmentRepository.save(appointmentEntity));
        }

        if (user.getRole() == UserRole.DOCTOR) {
            if (user.getDoctor() == null || !appointmentEntity.getDoctor().getId().equals(user.getDoctor().getId())) {
                throw new BadRequestException("Doctor can update only own appointments");
            }
            if (requestedStatus != AppointmentStatus.COMPLETED && requestedStatus != AppointmentStatus.CANCELLED) {
                throw new BadRequestException("Doctor can set only COMPLETED or CANCELLED status");
            }
            appointmentEntity.setStatus(requestedStatus);
            return appointmentMapper.toAppointment(appointmentRepository.save(appointmentEntity));
        }

        if (user.getRole() == UserRole.PATIENT) {
            if (user.getPatient() == null || !appointmentEntity.getPatient().getId().equals(user.getPatient().getId())) {
                throw new BadRequestException("Patient can update only own appointments");
            }
            if (requestedStatus != AppointmentStatus.SCHEDULED && requestedStatus != AppointmentStatus.CANCELLED) {
                throw new BadRequestException("Patient can set only SCHEDULED or CANCELLED status");
            }
            appointmentEntity.setStatus(requestedStatus);
            return appointmentMapper.toAppointment(appointmentRepository.save(appointmentEntity));
        }

        throw new BadRequestException("Role is not allowed to update appointment status");
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

    private void validateDoctorSchedule(AppointmentRequestDto requestDto) {
        DayOfWeek dayOfWeek = requestDto.getAppointmentDateTime().getDayOfWeek();
        LocalTime appointmentTime = requestDto.getAppointmentDateTime().toLocalTime();

        List<DoctorScheduleEntity> schedules = doctorScheduleRepository.findByDoctorIdAndDayOfWeek(
                requestDto.getDoctorId(),
                dayOfWeek
        );

        boolean appointmentFitsSchedule = schedules.stream()
                .anyMatch(schedule -> isTimeInsideSchedule(appointmentTime, schedule));

        if (!appointmentFitsSchedule) {
            throw new BadRequestException("Doctor does not work at the selected appointment time");
        }
    }

    private boolean isTimeInsideSchedule(LocalTime appointmentTime, DoctorScheduleEntity schedule) {
        return !appointmentTime.isBefore(schedule.getStartTime())
                && appointmentTime.isBefore(schedule.getEndTime());
    }

    private void validateDoctorAppointmentConflict(AppointmentRequestDto requestDto) {
        boolean appointmentExists = appointmentRepository.existsByDoctor_IdAndAppointmentDateTimeAndStatusIn(
                requestDto.getDoctorId(),
                requestDto.getAppointmentDateTime(),
                List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.COMPLETED)
        );

        if (appointmentExists) {
            throw new BadRequestException("Doctor already has an appointment at the selected time");
        }
    }

    private void validateDoctorAppointmentConflict(UUID appointmentId, AppointmentRequestDto requestDto) {
        boolean appointmentExists = appointmentRepository.existsByDoctor_IdAndAppointmentDateTimeAndIdNotAndStatusIn(
                requestDto.getDoctorId(),
                requestDto.getAppointmentDateTime(),
                appointmentId,
                List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.COMPLETED)
        );

        if (appointmentExists) {
            throw new BadRequestException("Doctor already has an appointment at the selected time");
        }
    }
}
