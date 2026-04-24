package com.healthflow.web;

import com.healthflow.dto.HealthResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<HealthResponseDto> getHealth() {
        HealthResponseDto response = new HealthResponseDto("OK", "HealthFlow");
        return ResponseEntity.ok(response);
    }
}
