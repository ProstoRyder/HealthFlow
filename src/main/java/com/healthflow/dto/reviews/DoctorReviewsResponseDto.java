package com.healthflow.dto.reviews;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class DoctorReviewsResponseDto {
    UUID doctorId;
    Double averageRating;
    Integer totalReviews;
    List<ReviewResponseDto> reviews;
}
