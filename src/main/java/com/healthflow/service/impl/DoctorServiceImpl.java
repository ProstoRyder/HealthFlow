package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.domain.Doctor;
import com.healthflow.dto.doctors.DoctorRequestDto;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.HospitalRepository;
import com.healthflow.repository.SpecialtyRepository;
import com.healthflow.repository.entity.DoctorEntity;
import com.healthflow.repository.entity.HospitalEntity;
import com.healthflow.repository.entity.SpecialtyEntity;
import com.healthflow.service.DoctorService;
import com.healthflow.service.mapper.DoctorMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final HospitalRepository hospitalRepository;
    private final DoctorMapper doctorMapper;

    @Override
    @Transactional
    public Doctor create(DoctorRequestDto requestDto) {
        SpecialtyEntity specialtyEntity = findSpecialtyById(requestDto.getSpecialtyId());
        HospitalEntity hospitalEntity = findHospitalById(requestDto.getHospitalId());
        DoctorEntity doctorEntity = doctorMapper.toEntity(requestDto);
        doctorEntity.setSpecialty(specialtyEntity);
        doctorEntity.setHospital(hospitalEntity);

        return doctorMapper.toDoctor(doctorRepository.save(doctorEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> getAll() {
        return doctorMapper.toDoctorList(doctorRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doctor> search(String query) {
        String normalizedQuery = normalize(query);

        if (normalizedQuery.isEmpty()) {
            return getAll();
        }

        List<Doctor> doctors = doctorMapper.toDoctorList(doctorRepository.findAll());
        List<Doctor> result = new ArrayList<>();

        for (Doctor doctor : doctors) {
            if (matchesFreeText(doctor, normalizedQuery)) {
                result.add(doctor);
            }
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Doctor getById(UUID id) {
        return doctorMapper.toDoctor(findDoctorById(id));
    }

    @Override
    @Transactional
    public Doctor update(UUID id, DoctorRequestDto requestDto) {
        DoctorEntity doctorEntity = findDoctorById(id);
        SpecialtyEntity specialtyEntity = findSpecialtyById(requestDto.getSpecialtyId());
        HospitalEntity hospitalEntity = findHospitalById(requestDto.getHospitalId());

        doctorMapper.updateEntityFromDto(requestDto, doctorEntity);
        doctorEntity.setSpecialty(specialtyEntity);
        doctorEntity.setHospital(hospitalEntity);

        return doctorMapper.toDoctor(doctorRepository.save(doctorEntity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        doctorRepository.delete(findDoctorById(id));
    }

    private DoctorEntity findDoctorById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor with id " + id + " not found"));
    }

    private SpecialtyEntity findSpecialtyById(UUID id) {
        return specialtyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialty with id " + id + " not found"));
    }

    private HospitalEntity findHospitalById(UUID id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital with id " + id + " not found"));
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }

    private boolean fuzzyMatch(String search, String target) {
        if (target.isEmpty()) {
            return false;
        }

        if (target.contains(search) || search.contains(target)) {
            return true;
        }

        if (commonPrefixLength(search, target) >= 6) {
            return true;
        }

        int maxDistance = search.length() <= 5 ? 1 : 2;
        return levenshteinDistance(search, target) <= maxDistance;
    }

    private boolean matchesFreeText(Doctor doctor, String query) {
        String firstName = normalize(doctor.getFirstName());
        String lastName = normalize(doctor.getLastName());
        String specialty = normalize(doctor.getSpecialty().getName());
        String fullName = (firstName + " " + lastName).trim();
        String reverseFullName = (lastName + " " + firstName).trim();

        String[] tokens = query.split("\\s+");
        for (String token : tokens) {
            if (token.isBlank()) {
                continue;
            }

            boolean tokenMatches =
                    fuzzyMatch(token, firstName)
                            || fuzzyMatch(token, lastName)
                            || fuzzyMatch(token, specialty)
                            || fuzzyMatch(token, fullName)
                            || fuzzyMatch(token, reverseFullName);

            if (!tokenMatches) {
                return false;
            }
        }

        return true;
    }

    private int commonPrefixLength(String left, String right) {
        int max = Math.min(left.length(), right.length());
        int count = 0;
        while (count < max && left.charAt(count) == right.charAt(count)) {
            count++;
        }
        return count;
    }

    private int levenshteinDistance(String left, String right) {
        int leftLength = left.length();
        int rightLength = right.length();

        int[] previous = new int[rightLength + 1];
        int[] current = new int[rightLength + 1];

        for (int j = 0; j <= rightLength; j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= leftLength; i++) {
            current[0] = i;
            char leftChar = left.charAt(i - 1);

            for (int j = 1; j <= rightLength; j++) {
                int cost = leftChar == right.charAt(j - 1) ? 0 : 1;
                int insert = current[j - 1] + 1;
                int delete = previous[j] + 1;
                int replace = previous[j - 1] + cost;
                current[j] = Math.min(Math.min(insert, delete), replace);
            }

            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[rightLength];
    }
}
