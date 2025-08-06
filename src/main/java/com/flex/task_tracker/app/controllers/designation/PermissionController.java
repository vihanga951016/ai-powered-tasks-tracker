package com.flex.task_tracker.app.controllers.designation;

import com.flex.task_tracker.app.entities.designation.Permission;
import com.flex.task_tracker.app.services.designation.PermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/create")
    @PreAuthorize("@securityService.hasAnyAccess('permission_management')")
    public ResponseEntity createPermission(@RequestBody Permission permission, HttpServletRequest request) {
        return permissionService.addPermission(permission, request);
    }

    @PostMapping("/update")
    @PreAuthorize("@securityService.hasAnyAccess('permission_management')")
    public ResponseEntity updatePermission(@RequestBody Permission permission, HttpServletRequest request) {
        return permissionService.updatePermission(permission, request);
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("@securityService.hasAnyAccess('permission_management')")
    public ResponseEntity deletePermission(@PathVariable Integer id, HttpServletRequest request) {
        return permissionService.deletePermission(id, request);
    }
}
