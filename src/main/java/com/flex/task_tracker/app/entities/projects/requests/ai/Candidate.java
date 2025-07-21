package com.flex.task_tracker.app.entities.projects.requests.ai;

import lombok.Data;

@Data
public class Candidate {
    private Content content;
    private String finishReason;
    private Double avgLogprobs;
}
