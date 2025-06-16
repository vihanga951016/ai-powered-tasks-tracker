package com.flex.task_tracker.app.entities;

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
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String subject;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private Date addedTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private Date deadline;
    @OneToOne
    @JoinColumn(name = "mainTaskId")
    private Task mainTasks;
    private double hours;

    //pending=0, in-progress=1, testing=2, documenting=3, completed=4
    private Integer status;
    private boolean restricted;
    private boolean deleted;

}
