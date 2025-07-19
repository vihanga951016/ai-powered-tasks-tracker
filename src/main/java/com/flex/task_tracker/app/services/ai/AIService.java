package com.flex.task_tracker.app.services.ai;

import com.flex.task_tracker.app.entities.http.requests.AIRequest;
import com.flex.task_tracker.app.entities.http.requests.ai.GeminiData;
import com.flex.task_tracker.app.entities.http.requests.ai.GeneratedProject;
import com.flex.task_tracker.app.repositories.TaskPlanRepository;
import com.flex.task_tracker.app.services.ai.helper.AIServiceHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.flex.task_tracker.common.constants.Messages.*;
import static com.flex.task_tracker.common.http.ReturnResponse.DATA;
import static com.flex.task_tracker.common.http.ReturnResponse.ERROR;


@Slf4j
@Component
@RequiredArgsConstructor
public class AIService {

    private final TaskPlanRepository repository;
    private final AIServiceHelper aiServiceHelper;

    @SneakyThrows
    public ResponseEntity generatePlan(AIRequest aiRequest, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (!aiServiceHelper.apiKeyCheck()) {
            return ERROR("AI Connection error: Missing API Key");
        }

        String instruction = STRUCTURE + aiRequest.getPrompt() + END;

        Map<String, Object> payload = aiServiceHelper.createPayload(instruction);

        HttpHeaders httpHeader = aiServiceHelper.createApiHeader();

        GeminiData geminiData = aiServiceHelper.getGoogleApiResponse(payload, httpHeader);

        String text = geminiData.getCandidates().get(0).getContent().getParts().get(0).getText();

        GeneratedProject generatedProject = aiServiceHelper.getGeneratedProjectByGeneratedText(text);

        return DATA(generatedProject != null ? generatedProject : REGENERATE);
    }

//    public ResponseEntity saveGeneratedPlan(GeneratedProject generatedProject, HttpServletRequest request) {
//        log.info(request.getRequestURI());
//
//
//    }
}
