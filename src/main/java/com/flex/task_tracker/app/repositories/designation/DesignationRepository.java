package com.flex.task_tracker.app.repositories.designation;

import com.flex.task_tracker.app.entities.designation.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Integer> {

    boolean existsByDesignationAndDeletedIsFalse(String designation);

    Designation getDesignationByIdAndDeletedIsFalse(Integer id);
}
