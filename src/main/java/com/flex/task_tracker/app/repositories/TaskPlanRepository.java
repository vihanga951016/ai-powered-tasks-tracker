package com.flex.task_tracker.app.repositories;

import com.flex.task_tracker.app.entities.http.TaskPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskPlanRepository extends JpaRepository<TaskPlan, Integer> {


}
