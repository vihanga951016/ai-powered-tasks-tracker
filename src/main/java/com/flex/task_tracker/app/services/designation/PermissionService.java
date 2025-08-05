package com.flex.task_tracker.app.services.designation;

import com.flex.task_tracker.app.entities.designation.Permission;
import com.flex.task_tracker.app.repositories.designation.PermissionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.flex.task_tracker.common.http.ReturnResponse.*;

@Slf4j
@Service
@SuppressWarnings("Duplicates")
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public ResponseEntity addPermission(Permission permission, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (permissionRepository
                .existsByPermissionAndDeletedIsFalse(permission.getPermission())){
            return ERROR("Permission already exist");
        }

        permissionRepository.save(permission);

        return SUCCESS("Permission saved");
    }

    public ResponseEntity updatePermission(Permission permission, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (permission.getId() == null){
            return BAD_REQUEST("Invalid permission id");
        }

        Permission exPermission = permissionRepository
                .getPermissionByIdAndDeletedIsFalse(permission.getId());

        if (exPermission == null) {
            return ERROR("Permission not found");
        }

        if (permission.getPermission() == null) {
            return SUCCESS("Nothing to update");
        }

        if (permissionRepository
                .existsByPermissionAndDeletedIsFalse(permission.getPermission())){
            return ERROR("Permission already exist");
        }

        exPermission.setPermission(permission.getPermission());

        permissionRepository.save(permission);

        return SUCCESS("Permission updated");
    }

    public ResponseEntity deletePermission(Integer id, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (id == null){
            return BAD_REQUEST("Invalid permission id");
        }

        Permission exPermission = permissionRepository
                .getPermissionByIdAndDeletedIsFalse(id);

        if (exPermission == null) {
            return ERROR("Permission not found");
        }

        exPermission.setDeleted(true);

        permissionRepository.save(exPermission);

        return SUCCESS("Permission deleted");
    }

}
