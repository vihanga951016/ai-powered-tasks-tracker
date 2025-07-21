package com.flex.task_tracker.app.repositories.project;

import com.flex.task_tracker.app.entities.projects.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    boolean existsByTitle(String name);
}
