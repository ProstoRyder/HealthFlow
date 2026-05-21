package com.healthflow.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Hospital {
    UUID id;
    String name;
    String address;
    String phoneNumber;
    String email;
    HospitalType type;
}
