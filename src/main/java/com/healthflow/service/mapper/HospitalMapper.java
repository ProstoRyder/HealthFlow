package com.healthflow.service.mapper;

import com.healthflow.domain.Hospital;
import com.healthflow.dto.hospitals.HospitalRequestDto;
import com.healthflow.dto.hospitals.HospitalResponseDto;
import com.healthflow.repository.entity.HospitalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    HospitalEntity toEntity(HospitalRequestDto requestDto);

    Hospital toHospital(HospitalEntity hospitalEntity);

    List<Hospital> toHospitalList(List<HospitalEntity> hospitalEntities);

    HospitalResponseDto toResponseDto(Hospital hospital);

    List<HospitalResponseDto> toResponseDtoList(List<Hospital> hospitals);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctors", ignore = true)
    void updateEntityFromDto(HospitalRequestDto requestDto, @MappingTarget HospitalEntity hospitalEntity);
}
