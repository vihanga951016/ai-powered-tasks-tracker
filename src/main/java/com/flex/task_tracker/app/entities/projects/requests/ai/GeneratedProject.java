package com.flex.task_tracker.app.entities.projects.requests.ai;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedProject {
    private String project;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Colombo")
    @Temporal(TemporalType.DATE)
    private Date deadline;
    private List<GeneratedTask> tasks;
}
