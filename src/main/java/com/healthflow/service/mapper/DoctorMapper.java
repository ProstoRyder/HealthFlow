package com.healthflow.service.mapper;

import com.healthflow.domain.Doctor;
import com.healthflow.dto.doctors.DoctorRequestDto;
import com.healthflow.dto.doctors.DoctorResponseDto;
import com.healthflow.repository.entity.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorEntity toEntity(DoctorRequestDto requestDto);

    Doctor toDoctor(DoctorEntity doctorEntity);

    List<Doctor> toDoctorList(List<DoctorEntity> doctorEntities);

    DoctorResponseDto toResponseDto(Doctor doctor);

    List<DoctorResponseDto> toResponseDtoList(List<Doctor> doctors);

    void updateEntityFromDto(DoctorRequestDto requestDto, @MappingTarget DoctorEntity doctorEntity);
}
