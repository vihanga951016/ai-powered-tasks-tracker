package com.flex.task_tracker.app.entities.projects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.flex.task_tracker.app.entities.projects.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String subject;
    @Column(length = 1000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private Date addedTime;
    private int duration;
    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;
    private boolean approved;
    private boolean deleted;

}
