package com.flex.task_tracker.app.repositories.project;

import com.flex.task_tracker.app.entities.projects.TaskSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskSkillRepository extends JpaRepository<TaskSkill, Integer> {
}
