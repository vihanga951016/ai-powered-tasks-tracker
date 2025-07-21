package com.flex.task_tracker.app.controllers.AI;

import com.flex.task_tracker.app.entities.projects.requests.AIRequest;
import com.flex.task_tracker.app.entities.projects.requests.ai.GeneratedProject;
import com.flex.task_tracker.app.services.ai.AIService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity generatePlan(@RequestBody AIRequest aiRequest, HttpServletRequest request) {
        return aiService.generatePlan(aiRequest, request);
    }

    @PostMapping("/save-plan")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity savePlan(@RequestBody GeneratedProject generatedProject, HttpServletRequest request) {
        return aiService.saveGeneratedPlan(generatedProject, request);
    }
}
