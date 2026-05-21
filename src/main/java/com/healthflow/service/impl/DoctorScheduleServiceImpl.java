package com.healthflow.service.impl;

import com.healthflow.common.BadRequestException;
import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.DoctorSchedule;
import com.healthflow.dto.doctorSchedules.DoctorScheduleRequestDto;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.DoctorScheduleRepository;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.DoctorScheduleEntity;
import com.healthflow.service.DoctorScheduleService;
import com.healthflow.service.mapper.DoctorScheduleMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper doctorScheduleMapper;

    @Override
    @Transactional
    public DoctorSchedule create(DoctorScheduleRequestDto requestDto) {
        validateTimeRange(requestDto);
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        DoctorScheduleEntity doctorScheduleEntity = doctorScheduleMapper.toEntity(requestDto);
        doctorScheduleEntity.setDoctor(doctorEntity);

        return doctorScheduleMapper.toDoctorSchedule(doctorScheduleRepository.save(doctorScheduleEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorSchedule> getAll() {
        return doctorScheduleMapper.toDoctorScheduleList(doctorScheduleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorSchedule getById(UUID id) {
        return doctorScheduleMapper.toDoctorSchedule(findDoctorScheduleById(id));
    }

    @Override
    @Transactional
    public DoctorSchedule update(UUID id, DoctorScheduleRequestDto requestDto) {
        validateTimeRange(requestDto);
        DoctorScheduleEntity doctorScheduleEntity = findDoctorScheduleById(id);
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        doctorScheduleMapper.updateEntityFromDto(requestDto, doctorScheduleEntity);
        doctorScheduleEntity.setDoctor(doctorEntity);

        return doctorScheduleMapper.toDoctorSchedule(doctorScheduleRepository.save(doctorScheduleEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        doctorScheduleRepository.delete(findDoctorScheduleById(id));
    }

    private void validateTimeRange(DoctorScheduleRequestDto requestDto) {
        if (!requestDto.getEndTime().isAfter(requestDto.getStartTime())) {
            throw new BadRequestException("End time must be after start time");
        }
    }

    private DoctorScheduleEntity findDoctorScheduleById(UUID id) {
        return doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor schedule with id " + id + " not found"));
    }

    private DoctorEntity findDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + id + " not found"));
    }
}
