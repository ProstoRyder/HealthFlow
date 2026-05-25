package com.healthflow.web;

import com.healthflow.dto.ai.AiChatRequestDto;
import com.healthflow.dto.ai.AiChatResponseDto;
import com.healthflow.service.AiAssistantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiAssistantService aiAssistantService;

    @PostMapping("/chat")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<AiChatResponseDto> chat(@Valid @RequestBody AiChatRequestDto requestDto) {
        String response = aiAssistantService.chat(requestDto.message());
        return ResponseEntity.ok(AiChatResponseDto.builder().message(response).build());
    }
}
