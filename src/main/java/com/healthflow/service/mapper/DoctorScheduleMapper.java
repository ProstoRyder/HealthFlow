package com.healthflow.service.mapper;

import com.healthflow.domain.DoctorSchedule;
import com.healthflow.dto.doctorSchedules.DoctorScheduleRequestDto;
import com.healthflow.dto.doctorSchedules.DoctorScheduleResponseDto;
import com.healthflow.repository.entity.DoctorScheduleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    DoctorScheduleEntity toEntity(DoctorScheduleRequestDto requestDto);

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(doctorScheduleEntity.getDoctor().getFirstName() + \" \" + doctorScheduleEntity.getDoctor().getLastName())")
    DoctorSchedule toDoctorSchedule(DoctorScheduleEntity doctorScheduleEntity);

    List<DoctorSchedule> toDoctorScheduleList(List<DoctorScheduleEntity> doctorScheduleEntities);

    DoctorScheduleResponseDto toResponseDto(DoctorSchedule doctorSchedule);

    List<DoctorScheduleResponseDto> toResponseDtoList(List<DoctorSchedule> doctorSchedules);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    void updateEntityFromDto(DoctorScheduleRequestDto requestDto, @MappingTarget DoctorScheduleEntity doctorScheduleEntity);
}
