package com.healthflow.dto.hospitals;

import com.healthflow.domain.HospitalType;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class HospitalResponseDto {
    UUID id;
    String name;
    String address;
    String phoneNumber;
    String email;
    HospitalType type;
}
