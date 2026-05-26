package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.common.BadRequestException;
import com.healthflow.domain.AppointmentStatus;
import com.healthflow.domain.Review;
import com.healthflow.dto.reviews.DoctorReviewsResponseDto;
import com.healthflow.dto.reviews.ReviewResponseDto;
import com.healthflow.dto.reviews.ReviewRequestDto;
import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.ReviewRepository;
import com.healthflow.repository.UserRepository;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.PatientEntity;
import com.healthflow.repository.entity.ReviewEntity;
import com.healthflow.repository.entity.UserEntity;
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
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public Review create(ReviewRequestDto requestDto) {
        if (requestDto.getPatientId() == null) {
            throw new BadRequestException("Patient id is required.");
        }

        PatientEntity patientEntity = findPatientById(requestDto.getPatientId());
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        ReviewEntity reviewEntity = reviewMapper.toEntity(requestDto);
        reviewEntity.setPatient(patientEntity);
        reviewEntity.setDoctor(doctorEntity);

        return reviewMapper.toReview(reviewRepository.save(reviewEntity));
    }

    @Override
    @Transactional
    public Review createAsPatient(String currentUserEmail, ReviewRequestDto requestDto) {
        UserEntity user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new BadRequestException("Current user was not found"));

        if (user.getPatient() == null) {
            throw new BadRequestException("Current user is not linked to a patient");
        }

        PatientEntity patientEntity = user.getPatient();
        DoctorEntity doctorEntity = findDoctorById(requestDto.getDoctorId());

        boolean hasCompletedAppointment = appointmentRepository.existsByPatient_IdAndDoctor_IdAndStatus(
                patientEntity.getId(),
                doctorEntity.getId(),
                AppointmentStatus.COMPLETED
        );

        if (!hasCompletedAppointment) {
            throw new BadRequestException("Review can be added only after completed appointment with this doctor");
        }

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
    public DoctorReviewsResponseDto getByDoctorId(UUID doctorId) {
        findDoctorById(doctorId);

        List<Review> reviews = reviewMapper.toReviewList(reviewRepository.findByDoctor_Id(doctorId));
        List<ReviewResponseDto> reviewDtos = reviewMapper.toResponseDtoList(reviews);

        double average = 0.0;
        if (!reviews.isEmpty()) {
            average = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            average = Math.round(average * 100.0) / 100.0;
        }

        return DoctorReviewsResponseDto.builder()
                .doctorId(doctorId)
                .averageRating(average)
                .totalReviews(reviews.size())
                .reviews(reviewDtos)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Review getById(UUID id) {
        return reviewMapper.toReview(findReviewById(id));
    }

    @Override
    @Transactional
    public Review update(UUID id, ReviewRequestDto requestDto) {
        if (requestDto.getPatientId() == null) {
            throw new BadRequestException("Patient id is required.");
        }

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
