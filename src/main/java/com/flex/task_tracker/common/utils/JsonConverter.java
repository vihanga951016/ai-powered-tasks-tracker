package com.flex.task_tracker.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flex.task_tracker.app.entities.http.requests.ai.GeneratedProject;

public class JsonConverter {
    public GeneratedProject convertJsonToProject(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, GeneratedProject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
