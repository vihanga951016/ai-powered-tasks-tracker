package com.flex.task_tracker.app.repositories.user;

import com.flex.task_tracker.app.entities.user.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SkillRepository extends JpaRepository<Skill, Integer> {

    @Query("SELECT s FROM Skill s " +
            "WHERE LOWER(s.skill) = LOWER(:skill) AND s.deleted = false")
    Skill checkSkill(@Param("skill") String skill);

}
