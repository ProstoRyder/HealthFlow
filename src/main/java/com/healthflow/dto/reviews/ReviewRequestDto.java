package com.healthflow.dto.reviews;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ReviewRequestDto {

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating must not exceed 5.")
    Integer rating;

    @Size(max = 1000, message = "Comment must not exceed 1000 characters.")
    String comment;

    @NotNull(message = "Patient id is required.")
    UUID patientId;

    @NotNull(message = "Doctor id is required.")
    UUID doctorId;
}
