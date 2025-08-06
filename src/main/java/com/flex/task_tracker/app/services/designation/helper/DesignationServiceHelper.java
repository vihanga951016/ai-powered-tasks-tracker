package com.flex.task_tracker.app.services.designation.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class DesignationServiceHelper {

    //todo: remove this
    @Autowired
    private CacheManager cacheManager;

    //todo: remove this
    public boolean isPermissionsCached(Integer designationId) {
        Cache cache = cacheManager.getCache("permissions");
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(designationId);
            return valueWrapper != null; // true = cached, false = not cached
        }
        return false; // cache doesn't exist
    }

}
