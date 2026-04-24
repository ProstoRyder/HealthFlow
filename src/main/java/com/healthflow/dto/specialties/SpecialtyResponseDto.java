package com.healthflow.dto.specialties;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class SpecialtyResponseDto {
    UUID id;
    String name;
    String description;
}
