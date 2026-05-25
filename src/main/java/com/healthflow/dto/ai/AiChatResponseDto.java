package com.healthflow.dto.ai;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AiChatResponseDto {
    String message;
}
