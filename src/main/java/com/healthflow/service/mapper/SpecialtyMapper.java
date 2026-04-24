package com.healthflow.service.mapper;

import com.healthflow.domain.Specialty;
import com.healthflow.dto.specialties.SpecialtyRequestDto;
import com.healthflow.dto.specialties.SpecialtyResponseDto;
import com.healthflow.repository.entity.SpecialtyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    SpecialtyEntity toEntity(SpecialtyRequestDto requestDto);

    Specialty toSpecialty(SpecialtyEntity specialtyEntity);

    List<Specialty> toSpecialtyList(List<SpecialtyEntity> specialtyEntities);

    SpecialtyResponseDto toResponseDto(Specialty specialty);

    List<SpecialtyResponseDto> toResponseDtoList(List<Specialty> specialties);

    void updateEntityFromDto(SpecialtyRequestDto requestDto, @MappingTarget SpecialtyEntity specialtyEntity);
}
