package com.healthflow.dto.reviews;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ReviewResponseDto {
    UUID id;
    Integer rating;
    String comment;
    UUID patientId;
    String patientFullName;
    UUID doctorId;
    String doctorFullName;
}
