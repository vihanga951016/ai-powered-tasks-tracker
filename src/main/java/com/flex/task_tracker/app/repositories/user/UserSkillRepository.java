package com.flex.task_tracker.app.repositories.user;

import com.flex.task_tracker.app.entities.user.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSkillRepository extends JpaRepository<UserSkill, Integer> {
}
