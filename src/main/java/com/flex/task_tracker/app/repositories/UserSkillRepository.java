package com.flex.task_tracker.app.repositories;

import com.flex.task_tracker.app.entities.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkill, Integer> {
}
