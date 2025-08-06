package com.flex.task_tracker.app.controllers.designation;

import com.flex.task_tracker.app.entities.designation.Designation;
import com.flex.task_tracker.app.entities.designation.requests.AssignPermissions;
import com.flex.task_tracker.app.services.designation.DesignationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/designation")
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService designationService;

    @PostMapping("/create")
    @PreAuthorize("@securityService.hasAnyAccess('designation_management')")
    public ResponseEntity createDesignation(@RequestBody Designation designation,
                                            HttpServletRequest request) {
        return designationService.createDesignation(designation, request);
    }

    @PostMapping("/update")
    @PreAuthorize("@securityService.hasAnyAccess('designation_management')")
    public ResponseEntity updateDesignation(@RequestBody Designation designation,
                                            HttpServletRequest request) {
        return designationService.updateDesignation(designation, request);
    }

    @PostMapping("/manage-permissions")
    @PreAuthorize("@securityService.hasAnyAccess('designation_management')")
    public ResponseEntity managePermissions(@RequestBody AssignPermissions assignPermissions,
                                            HttpServletRequest request) {
        return designationService.managePermissionForDesignation(assignPermissions, request);
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("@securityService.hasAnyAccess('designation_management')")
    public ResponseEntity deleteDesignation(@PathVariable Integer id,
                                            HttpServletRequest request) {
        return designationService.deleteDesignation(id, request);
    }
}
