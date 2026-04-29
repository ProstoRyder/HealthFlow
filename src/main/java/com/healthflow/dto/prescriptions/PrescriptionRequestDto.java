package com.healthflow.dto.prescriptions;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PrescriptionRequestDto {

    @NotBlank(message = "Medicine name is required.")
    @Size(max = 150, message = "Medicine name must not exceed 150 characters.")
    String medicineName;

    @NotBlank(message = "Dosage is required.")
    @Size(max = 100, message = "Dosage must not exceed 100 characters.")
    String dosage;

    @NotBlank(message = "Instructions are required.")
    @Size(max = 1000, message = "Instructions must not exceed 1000 characters.")
    String instructions;

    @NotNull(message = "Duration days is required.")
    @Min(value = 1, message = "Duration days must be at least 1.")
    Integer durationDays;

    @NotNull(message = "Consultation id is required.")
    UUID consultationId;
}
