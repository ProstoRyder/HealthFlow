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

    PatientEntity toEntity(PatientRequestDto requestDto);

    Patient toPatient(PatientEntity patientEntity);

    List<Patient> toPatientList(List<PatientEntity> patientEntities);

    PatientResponseDto toResponseDto(Patient patient);

    List<PatientResponseDto> toResponseDtoList(List<Patient> patients);

    void updateEntityFromDto(PatientRequestDto requestDto, @MappingTarget PatientEntity patientEntity);
}
