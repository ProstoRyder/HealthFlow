package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Review;
import com.healthflow.dto.reviews.ReviewRequestDto;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.ReviewRepository;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.PatientEntity;
import com.healthflow.repository.entity.ReviewEntity;
import com.healthflow.service.ReviewService;
import com.healthflow.service.mapper.ReviewMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public Review create(ReviewRequestDto requestDto) {
        PatientEntity patientEntity = findPatientById(requestDto.getPatientId());
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        ReviewEntity reviewEntity = reviewMapper.toEntity(requestDto);
        reviewEntity.setPatient(patientEntity);
        reviewEntity.setDoctor(doctorEntity);

        return reviewMapper.toReview(reviewRepository.save(reviewEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAll() {
        return reviewMapper.toReviewList(reviewRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Review getById(UUID id) {
        return reviewMapper.toReview(findReviewById(id));
    }

    @Override
    @Transactional
    public Review update(UUID id, ReviewRequestDto requestDto) {
        ReviewEntity reviewEntity = findReviewById(id);
        PatientEntity patientEntity = findPatientById(requestDto.getPatientId());
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        reviewMapper.updateEntityFromDto(requestDto, reviewEntity);
        reviewEntity.setPatient(patientEntity);
        reviewEntity.setDoctor(doctorEntity);

        return reviewMapper.toReview(reviewRepository.save(reviewEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        reviewRepository.delete(findReviewById(id));
    }

    private ReviewEntity findReviewById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + id + " not found"));
    }

    private PatientEntity findPatientById(UUID id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient with id " + id + " not found"));
    }

    private DoctorEntity findDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + id + " not found"));
    }
}
