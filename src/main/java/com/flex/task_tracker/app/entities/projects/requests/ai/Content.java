package com.flex.task_tracker.app.entities.projects.requests.ai;

import lombok.Data;

import java.util.List;

@Data
public class Content {
    private List<Part> parts;
    private String role;
}
