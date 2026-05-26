package com.healthflow.service;

import com.healthflow.domain.Review;
import com.healthflow.dto.reviews.DoctorReviewsResponseDto;
import com.healthflow.dto.reviews.ReviewRequestDto;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    Review create(ReviewRequestDto requestDto);

    Review createAsPatient(String currentUserEmail, ReviewRequestDto requestDto);

    List<Review> getAll();

    DoctorReviewsResponseDto getByDoctorId(UUID doctorId);

    Review getById(UUID id);

    Review update(UUID id, ReviewRequestDto requestDto);

    void delete(UUID id);
}
