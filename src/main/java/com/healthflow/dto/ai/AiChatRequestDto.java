package com.healthflow.dto.ai;

import jakarta.validation.constraints.NotBlank;

public record AiChatRequestDto(
        @NotBlank(message = "Message is required")
        String message
) {
}
