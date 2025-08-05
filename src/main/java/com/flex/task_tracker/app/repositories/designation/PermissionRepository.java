package com.flex.task_tracker.app.repositories.designation;

import com.flex.task_tracker.app.entities.designation.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    boolean existsByIdAndDeletedIsFalse(Integer permission);

    boolean existsByPermissionAndDeletedIsFalse(String permission);

    Permission getPermissionByIdAndDeletedIsFalse(Integer id);

    @Query("SELECT p.id FROM Permission p WHERE p.id IN :ids AND p.deleted = false")
    List<Integer> findAllValidIdsByIds(@Param("ids") List<Integer> ids);

}
