package com.flex.task_tracker.app.repositories.designation;

import com.flex.task_tracker.app.entities.designation.DesignationPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesignationPermissionRepository extends JpaRepository<DesignationPermission, Integer> {

    @Query("SELECT dp FROM DesignationPermission dp WHERE dp.designation.id=:designationId " +
            "and dp.designation.deleted = false and dp.permission.deleted = false")
    List<DesignationPermission> getAllDesignationPermissions(@Param("designationId") Integer designationId);

    @Query("SELECT dp FROM DesignationPermission dp WHERE dp.designation.id=:designationId " +
            "and dp.permission.permission <> 'permit_this' and dp.designation.deleted = false " +
            "and dp.permission.deleted = false")
    List<DesignationPermission> getAllEditableDesignationPermissions(@Param("designationId") Integer designationId);
}
