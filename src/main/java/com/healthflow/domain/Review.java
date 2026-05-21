package com.healthflow.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class Review {
    UUID id;
    Integer rating;
    String comment;
    UUID patientId;
    String patientFullName;
    UUID doctorId;
    String doctorFullName;
}
