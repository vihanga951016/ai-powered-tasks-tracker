package com.flex.task_tracker.app.services.designation.caching;

import com.flex.task_tracker.app.entities.designation.DesignationPermission;
import com.flex.task_tracker.app.repositories.designation.DesignationPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@SuppressWarnings("Duplicates")
@RequiredArgsConstructor
public class DesignationCacheService {

    private final DesignationPermissionRepository designationPermissionRepository;

    @Autowired
    private CacheManager cacheManager;

    @Cacheable(value = "permissions", key = "#designationId")
    public List<DesignationPermission> getAllPermissionsForDesignation(Integer designationId) {
        return designationPermissionRepository.getAllDesignationPermissions(designationId);
    }

    @CacheEvict(value = "permissions", key = "#designationId")
    public void evictPermissionsCache(Integer designationId) {
        // This method clears the cached permissions for the given designationId.
        // No implementation needed; the annotation handles eviction.
    }

    public boolean isPermissionsCached(Integer designationId) {
        Cache cache = cacheManager.getCache("permissions");
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(designationId);
            return valueWrapper != null; // true = cached, false = not cached
        }
        return false; // cache doesn't exist
    }
}
