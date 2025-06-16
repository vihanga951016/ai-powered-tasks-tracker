package com.flex.task_tracker.app.repositories;

import com.flex.task_tracker.app.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
