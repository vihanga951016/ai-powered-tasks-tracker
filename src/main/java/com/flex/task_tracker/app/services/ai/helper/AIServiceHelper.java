package com.flex.task_tracker.app.services.ai.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flex.task_tracker.app.entities.projects.requests.ai.GeminiData;
import com.flex.task_tracker.app.entities.projects.requests.ai.GeneratedProject;
import com.flex.task_tracker.common.utils.JsonConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AIServiceHelper {

    @Value("${google.ai.key}")
    private String googleAiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean apiKeyCheck() {
        return googleAiKey != null;
    }

    public Map<String, Object> createPayload(String instructions) {
        log.info("creating payload...");
        Map<String, Object> part = new HashMap<>();
        part.put("text", instructions);

        List<Map<String, Object>> parts = new ArrayList<>();
        parts.add(part);

        Map<String, Object> content = new HashMap<>();
        content.put("parts", parts);

        List<Map<String, Object>> contents = new ArrayList<>();
        contents.add(content);

        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", contents);

        log.info("payload created.");
        return payload;
    }

    public HttpHeaders createApiHeader() {
        log.info("creating api header...");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        log.info("headers created.");
        return headers;
    }

    @SneakyThrows
    public GeminiData getGoogleApiResponse(Map<String, Object> payload, HttpHeaders httpHeaders) {
        log.info("getting google api response...");
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, httpHeaders);

        ResponseEntity<String> response = restTemplate.postForEntity(googleAiKey, entity, String.class);

        log.info("got the response.");
        return objectMapper.readValue(response.getBody(), GeminiData.class);
    }

    public GeneratedProject getGeneratedProjectByGeneratedText(String text) {
        log.info("formatting text to json...");
        String cleanedJson = text.replaceAll("```json", "").replaceAll("```", "").trim();

        JsonConverter converter = new JsonConverter();

        log.info("json created.");
        return converter.convertJsonToProject(cleanedJson);
    }
}
