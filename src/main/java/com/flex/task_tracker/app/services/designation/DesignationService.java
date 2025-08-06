package com.flex.task_tracker.app.services.designation;

import com.flex.task_tracker.app.entities.designation.Designation;
import com.flex.task_tracker.app.entities.designation.DesignationPermission;
import com.flex.task_tracker.app.entities.designation.Permission;
import com.flex.task_tracker.app.entities.designation.requests.AssignPermissions;
import com.flex.task_tracker.app.repositories.designation.DesignationPermissionRepository;
import com.flex.task_tracker.app.repositories.designation.DesignationRepository;
import com.flex.task_tracker.app.repositories.designation.PermissionRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.flex.task_tracker.common.http.ReturnResponse.*;

@Slf4j
@Service
@SuppressWarnings("Duplicates")
@RequiredArgsConstructor
public class DesignationService {

    private final DesignationRepository designationRepository;
    private final PermissionRepository permissionRepository;
    private final DesignationPermissionRepository designationPermissionRepository;

    public ResponseEntity createDesignation(Designation designation, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (designationRepository
                .existsByDesignationAndDeletedIsFalse(designation.getDesignation())){
            return ERROR("Designation already exist");
        }

        Permission defaultPermission = permissionRepository
                .getPermissionByPermissionAndDeletedIsFalse("permit_this");

        if (defaultPermission == null) {
            Permission permitPermission = Permission.builder()
                    .permission("permit_this")
                    .deleted(false)
                    .build();

            defaultPermission = permissionRepository.save(permitPermission);
        }

        Designation newDesignation = designationRepository.save(designation);

        DesignationPermission designationPermission = DesignationPermission.builder()
                .designation(newDesignation)
                .permission(defaultPermission)
                .build();

        designationPermissionRepository.save(designationPermission);

        return SUCCESS("Designation saved");
    }

    public ResponseEntity updateDesignation(Designation designation, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (designation.getId() == null) {
            return BAD_REQUEST("Invalid designation id");
        }

        Designation exDesignation = designationRepository
                .getDesignationByIdAndDeletedIsFalse(designation.getId());

        if (exDesignation == null) {
            return ERROR("Designation not found");
        }

        if (designation.getDesignation() != null
                && !designation.getDesignation().isEmpty()
                && designation.getDesignation().equals(exDesignation.getDesignation())) {
            return SUCCESS("Nothing to update");
        }

        designationRepository.save(designation);

        return SUCCESS("Designation saved");
    }

    @CacheEvict(value = "permissions", key = "#assignPermissions.designationId")
    public ResponseEntity managePermissionForDesignation(AssignPermissions assignPermissions, HttpServletRequest request) {
        log.info(request.getRequestURI());

        Designation exDesignation = designationRepository
                .getDesignationByIdAndDeletedIsFalse(assignPermissions.getDesignationId());

        if (exDesignation == null) {
            return ERROR("Designation not found");
        }

        // Fetch all existing permission except 'permit_this' entities for the designation
        List<DesignationPermission> allPermissionsForDesignation = designationPermissionRepository
                .getAllEditableDesignationPermissions(assignPermissions.getDesignationId());

        List<Integer> requestedPermissionIds = assignPermissions.getPermissions();

        if (requestedPermissionIds == null || requestedPermissionIds.isEmpty()) {
            designationPermissionRepository.deleteAll(allPermissionsForDesignation);
            return SUCCESS("All permissions removed");
        }

        // Fetch all valid permission IDs from DB in one query
        List<Integer> validPermissionIds = permissionRepository.findAllValidIdsByIds(requestedPermissionIds);

        // Check for invalid permissions
        List<Integer> invalidPermissions = requestedPermissionIds.stream()
                .filter(id -> !validPermissionIds.contains(id))
                .toList();

        if (!invalidPermissions.isEmpty()) {
            log.error("Invalid permission IDs: {}", invalidPermissions);
            return BAD_REQUEST("Invalid permissions");
        }

        // Prepare sets for faster lookups
        Set<Integer> requestedSet = new HashSet<>(requestedPermissionIds);
        Set<Integer> existingSet = allPermissionsForDesignation.stream()
                .map(dp -> dp.getPermission().getId())
                .collect(Collectors.toSet());

        // Determine which to add
        List<DesignationPermission> toAdd = requestedSet.stream()
                .filter(id -> !existingSet.contains(id))
                .map(id -> DesignationPermission.builder()
                        .designation(exDesignation)
                        .permission(new Permission(id))
                        .build())
                .toList();

        // Determine which to delete
        List<DesignationPermission> toRemove = allPermissionsForDesignation.stream()
                .filter(dp -> !requestedSet.contains(dp.getPermission().getId()))
                .toList();

        if (!toAdd.isEmpty()) {
            designationPermissionRepository.saveAll(toAdd);
        }

        if (!toRemove.isEmpty()) {
            designationPermissionRepository.deleteAll(toRemove);
        }

        return SUCCESS("Permissions successfully updated");
    }

    public ResponseEntity deleteDesignation(Integer id, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (id == null){
            return BAD_REQUEST("Invalid designation id");
        }

        Designation exDesignation = designationRepository
                .getDesignationByIdAndDeletedIsFalse(id);

        if (exDesignation == null) {
            return ERROR("Designation not found");
        }

        exDesignation.setDeleted(true);

        designationRepository.save(exDesignation);

        return SUCCESS("Designation deleted");
    }
}
