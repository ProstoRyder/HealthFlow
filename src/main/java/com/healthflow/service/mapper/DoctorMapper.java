package com.healthflow.service.mapper;

import com.healthflow.domain.Doctor;
import com.healthflow.dto.doctors.DoctorRequestDto;
import com.healthflow.dto.doctors.DoctorResponseDto;
import com.healthflow.repository.entity.DoctorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specialty", ignore = true)
    @Mapping(target = "hospital", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    DoctorEntity toEntity(DoctorRequestDto requestDto);

    Doctor toDoctor(DoctorEntity doctorEntity);

    List<Doctor> toDoctorList(List<DoctorEntity> doctorEntities);

    @Mapping(target = "specialtyId", source = "specialty.id")
    @Mapping(target = "specialtyName", source = "specialty.name")
    @Mapping(target = "hospitalId", source = "hospital.id")
    @Mapping(target = "hospitalName", source = "hospital.name")
    DoctorResponseDto toResponseDto(Doctor doctor);

    List<DoctorResponseDto> toResponseDtoList(List<Doctor> doctors);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specialty", ignore = true)
    @Mapping(target = "hospital", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    void updateEntityFromDto(DoctorRequestDto requestDto, @MappingTarget DoctorEntity doctorEntity);
}
