package com.flex.task_tracker.app.repositories.project;

import com.flex.task_tracker.app.entities.projects.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
