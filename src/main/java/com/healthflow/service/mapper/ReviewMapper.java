package com.healthflow.service.mapper;

import com.healthflow.domain.Review;
import com.healthflow.dto.reviews.ReviewRequestDto;
import com.healthflow.dto.reviews.ReviewResponseDto;
import com.healthflow.repository.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    ReviewEntity toEntity(ReviewRequestDto requestDto);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientFullName", expression = "java(reviewEntity.getPatient().getFirstName() + \" \" + reviewEntity.getPatient().getLastName())")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorFullName", expression = "java(reviewEntity.getDoctor().getFirstName() + \" \" + reviewEntity.getDoctor().getLastName())")
    Review toReview(ReviewEntity reviewEntity);

    List<Review> toReviewList(List<ReviewEntity> reviewEntities);

    ReviewResponseDto toResponseDto(Review review);

    List<ReviewResponseDto> toResponseDtoList(List<Review> reviews);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    void updateEntityFromDto(ReviewRequestDto requestDto, @MappingTarget ReviewEntity reviewEntity);
}
