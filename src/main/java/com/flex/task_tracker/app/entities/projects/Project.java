package com.flex.task_tracker.app.entities.projects;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Colombo")
    @Temporal(TemporalType.DATE)
    private Date deadline;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private Date addedTime;
    private int status;

}
