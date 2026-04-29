package com.healthflow.service.mapper;

import com.healthflow.domain.Consultation;
import com.healthflow.dto.consultations.ConsultationRequestDto;
import com.healthflow.dto.consultations.ConsultationResponseDto;
import com.healthflow.repository.entity.ConsultationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsultationMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    ConsultationEntity toEntity(ConsultationRequestDto requestDto);

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "patientId", source = "appointment.patient.id")
    @Mapping(target = "patientFullName", expression = "java(consultationEntity.getAppointment().getPatient().getFirstName() + \" \" + consultationEntity.getAppointment().getPatient().getLastName())")
    @Mapping(target = "doctorId", source = "appointment.doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(consultationEntity.getAppointment().getDoctor().getFirstName() + \" \" + consultationEntity.getAppointment().getDoctor().getLastName())")
    Consultation toConsultation(ConsultationEntity consultationEntity);

    List<Consultation> toConsultationList(List<ConsultationEntity> consultationEntities);

    ConsultationResponseDto toResponseDto(Consultation consultation);

    List<ConsultationResponseDto> toResponseDtoList(List<Consultation> consultations);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointment", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    void updateEntityFromDto(ConsultationRequestDto requestDto, @MappingTarget ConsultationEntity consultationEntity);
}
