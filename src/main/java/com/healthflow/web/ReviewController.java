package com.healthflow.web;

import com.healthflow.domain.Review;
import com.healthflow.dto.reviews.ReviewRequestDto;
import com.healthflow.dto.reviews.ReviewResponseDto;
import com.healthflow.service.ReviewService;
import com.healthflow.service.mapper.ReviewMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ReviewResponseDto> create(@Valid @RequestBody ReviewRequestDto requestDto) {
        Review review = reviewService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewMapper.toResponseDto(review));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getAll() {
        return ResponseEntity.ok(reviewMapper.toResponseDtoList(reviewService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewMapper.toResponseDto(reviewService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewRequestDto requestDto
    ) {
        return ResponseEntity.ok(reviewMapper.toResponseDto(reviewService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
