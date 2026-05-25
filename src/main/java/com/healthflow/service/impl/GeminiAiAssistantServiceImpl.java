package com.healthflow.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthflow.service.AiAssistantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class GeminiAiAssistantServiceImpl implements AiAssistantService {

    private static final String DISCLAIMER =
            "Асистент не надає медичних рекомендацій. Для діагностики та лікування зверніться до лікаря.";
    private static final String SAFE_REDIRECT =
            "Я не можу надавати медичні рекомендації, діагнози або дозування. "
                    + "Можу допомогти з навігацією по HealthFlow: пошук лікаря, запис на прийом, перегляд консультацій і призначень. "
                    + DISCLAIMER;

    private static final List<String> MEDICAL_RISK_KEYWORDS = List.of(
            "дозув", "доза", "лікуван", "лікувати", "що приймати", "що випити", "признач мені",
            "рецепт", "діагноз", "які ліки", "what to take", "dosage", "dose", "treatment",
            "diagnosis", "prescription"
    );

    private static final Pattern DOSAGE_PATTERN =
            Pattern.compile("\\b\\d+\\s?(mg|мг|ml|мл|таблет|tablets?)\\b", Pattern.CASE_INSENSITIVE);

    @Value("${gemini.api-key:}")
    private String apiKey;

    @Value("${gemini.model:gemini-1.5-flash}")
    private String model;

    @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}")
    private String baseUrl;

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public String chat(String userMessage) {
        if (isMedicalRisk(userMessage)) {
            return SAFE_REDIRECT;
        }

        if (apiKey == null || apiKey.isBlank()) {
            return "AI тимчасово недоступний: не налаштовано Gemini API key. " + DISCLAIMER;
        }

        String raw = askGemini(userMessage);
        if (isUnsafeResponse(raw)) {
            return SAFE_REDIRECT;
        }

        return raw + "\n\n" + DISCLAIMER;
    }

    private String askGemini(String userMessage) {
        try {
            String url = baseUrl + "/models/" + URLEncoder.encode(model, StandardCharsets.UTF_8)
                    + ":generateContent?key=" + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

            String body = """
                    {
                      "system_instruction": {
                        "parts": [
                          {
                            "text": "You are HealthFlow assistant. You can help with app navigation and organizational questions about bookings, doctors, consultations and profile actions. Never provide diagnosis, treatment, dosage, prescriptions, or medication recommendations. If user asks for medical advice, refuse and suggest contacting a licensed doctor or emergency services."
                          }
                        ]
                      },
                      "contents": [
                        {
                          "role": "user",
                          "parts": [
                            {
                              "text": %s
                            }
                          ]
                        }
                      ]
                    }
                    """.formatted(objectMapper.writeValueAsString(userMessage));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(20))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                return "AI тимчасово недоступний. Спробуйте пізніше. " + DISCLAIMER;
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            String text = textNode.asText("").trim();
            if (text.isEmpty()) {
                return "AI тимчасово недоступний. Спробуйте пізніше. " + DISCLAIMER;
            }

            return text;
        } catch (Exception exception) {
            return "AI тимчасово недоступний. Спробуйте пізніше. " + DISCLAIMER;
        }
    }

    private boolean isMedicalRisk(String message) {
        String normalized = normalize(message);
        for (String keyword : MEDICAL_RISK_KEYWORDS) {
            if (normalized.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean isUnsafeResponse(String response) {
        String normalized = normalize(response);
        if (isSafeRefusal(normalized)) {
            return false;
        }
        return DOSAGE_PATTERN.matcher(response).find();
    }

    private boolean isSafeRefusal(String normalizedResponse) {
        return normalizedResponse.contains("cannot provide medical")
                || normalizedResponse.contains("can't provide medical")
                || normalizedResponse.contains("не можу надавати медич");
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().toLowerCase(Locale.ROOT);
    }
}
