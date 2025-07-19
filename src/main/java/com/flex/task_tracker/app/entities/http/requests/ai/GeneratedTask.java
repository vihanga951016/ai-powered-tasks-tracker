package com.flex.task_tracker.app.entities.http.requests.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedTask {
    private String tasksTitle;
    private String description;
    private int duration;
    private List<String> skills;
}
