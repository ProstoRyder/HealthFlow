package com.healthflow.web;

import com.healthflow.domain.Review;
import com.healthflow.dto.reviews.DoctorReviewsResponseDto;
import com.healthflow.dto.reviews.ReviewRequestDto;
import com.healthflow.dto.reviews.ReviewResponseDto;
import com.healthflow.service.ReviewService;
import com.healthflow.service.mapper.ReviewMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public ResponseEntity<ReviewResponseDto> create(
            Authentication authentication,
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        Review review;
        boolean isPatient = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_PATIENT".equals(authority.getAuthority()));

        if (isPatient) {
            review = reviewService.createAsPatient(authentication.getName(), requestDto);
        } else {
            review = reviewService.create(requestDto);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reviewMapper.toResponseDto(review));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReviewResponseDto>> getAll() {
        return ResponseEntity.ok(reviewMapper.toResponseDtoList(reviewService.getAll()));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorReviewsResponseDto> getByDoctor(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(reviewService.getByDoctorId(doctorId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewMapper.toResponseDto(reviewService.getById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        return ResponseEntity.ok(reviewMapper.toResponseDto(reviewService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
