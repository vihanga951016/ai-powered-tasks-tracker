package com.flex.task_tracker.app.entities.projects.requests.ai;

import lombok.Data;

import java.util.List;

@Data
public class GeminiData {
    private List<Candidate> candidates;
    private UsageMetadata usageMetadata;
    private String modelVersion;
    private String responseId;
}
