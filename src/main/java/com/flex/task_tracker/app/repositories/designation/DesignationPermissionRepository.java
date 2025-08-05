package com.flex.task_tracker.app.repositories.designation;

import com.flex.task_tracker.app.entities.designation.DesignationPermission;
import com.flex.task_tracker.app.entities.designation.requests.DTO.DesignationPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DesignationPermissionRepository extends JpaRepository<DesignationPermission, Integer> {

    @Query("SELECT dp FROM DesignationPermission dp WHERE dp.designation.id=:designationId and dp.designation.deleted = false and dp.permission.deleted = false")
    List<DesignationPermission> getAllDesignationPermissions(@Param("designationId") Integer designationId);

    @Query("SELECT d FROM DesignationPermission d " +
            "WHERE d.designation.id=:designationId and d.permission.id=:permissionId and d.designation.deleted = false " +
            "and d.permission.deleted = false")
    DesignationPermission getPermissionByDesignation(@Param("designationId") Integer designation,
                                                       @Param("permissionId") Integer permission);
}
