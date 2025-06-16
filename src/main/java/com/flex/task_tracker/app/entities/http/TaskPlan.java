package com.flex.task_tracker.app.entities.http;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task_plan")
public class TaskPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String project;
    private String section;
    private String taskTitle;
    @Column(length = 1000)
    private String description;
    private String estimatedHours;
    private String requiredSkills;
}
