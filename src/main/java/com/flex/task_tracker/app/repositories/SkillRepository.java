package com.flex.task_tracker.app.repositories;

import com.flex.task_tracker.app.entities.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}
