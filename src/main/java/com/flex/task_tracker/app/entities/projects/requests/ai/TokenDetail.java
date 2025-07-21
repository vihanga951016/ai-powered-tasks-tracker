package com.flex.task_tracker.app.entities.projects.requests.ai;

import lombok.Data;

@Data
public class TokenDetail {
    private String modality;
    private int tokenCount;
}
