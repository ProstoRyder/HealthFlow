package com.healthflow.service.mapper;

import com.healthflow.domain.Patient;
import com.healthflow.dto.patients.PatientRequestDto;
import com.healthflow.dto.patients.PatientResponseDto;
import com.healthflow.repository.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    PatientEntity toEntity(PatientRequestDto requestDto);

    Patient toPatient(PatientEntity patientEntity);

    List<Patient> toPatientList(List<PatientEntity> patientEntities);

    PatientResponseDto toResponseDto(Patient patient);

    List<PatientResponseDto> toResponseDtoList(List<Patient> patients);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    void updateEntityFromDto(PatientRequestDto requestDto, @MappingTarget PatientEntity patientEntity);
}
