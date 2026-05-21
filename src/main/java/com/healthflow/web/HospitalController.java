package com.healthflow.web;

import com.healthflow.domain.Hospital;
import com.healthflow.dto.hospitals.HospitalRequestDto;
import com.healthflow.dto.hospitals.HospitalResponseDto;
import com.healthflow.service.HospitalService;
import com.healthflow.service.mapper.HospitalMapper;
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
@RequestMapping("/api/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;
    private final HospitalMapper hospitalMapper;

    public HospitalController(HospitalService hospitalService, HospitalMapper hospitalMapper) {
        this.hospitalService = hospitalService;
        this.hospitalMapper = hospitalMapper;
    }

    @PostMapping
    public ResponseEntity<HospitalResponseDto> create(@Valid @RequestBody HospitalRequestDto requestDto) {
        Hospital hospital = hospitalService.create(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalMapper.toResponseDto(hospital));
    }

    @GetMapping
    public ResponseEntity<List<HospitalResponseDto>> getAll() {
        return ResponseEntity.ok(hospitalMapper.toResponseDtoList(hospitalService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(hospitalMapper.toResponseDto(hospitalService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponseDto> update(
            @PathVariable UUID id,
            @Valid @RequestBody HospitalRequestDto requestDto
    ) {
        return ResponseEntity.ok(hospitalMapper.toResponseDto(hospitalService.update(id, requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        hospitalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
