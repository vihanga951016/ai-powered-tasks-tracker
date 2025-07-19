package com.flex.task_tracker.app.controllers.AI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flex.task_tracker.app.entities.http.requests.AIRequest;
import com.flex.task_tracker.app.services.ai.AIService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/get")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity generatePlan(@RequestBody AIRequest aiRequest, HttpServletRequest request) {
        return aiService.generatePlan(aiRequest, request);
    }
}
