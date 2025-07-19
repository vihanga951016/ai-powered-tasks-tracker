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
public class GeneratedProject {
    private String project;
    private List<GeneratedTask> tasks;
}
