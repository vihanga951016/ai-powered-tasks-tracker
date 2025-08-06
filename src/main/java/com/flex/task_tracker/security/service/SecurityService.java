package com.flex.task_tracker.security.service;

import com.flex.task_tracker.common.constants.Colors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component("securityService")
public class SecurityService {

    public boolean hasAnyAccess(String... permissions) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            log.warn(Colors.YELLOW + "Authentication: " + auth + Colors.RESET);
            if (auth != null) log.warn(Colors.YELLOW + "Authentication Status: "
                    + auth.isAuthenticated() + Colors.RESET);

            return false;
        }

        // ✅ Allow all access if SUPER_ADMIN
        boolean isSuperAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("SUPER_ADMIN"));

        if (isSuperAdmin) {
            log.info("SUPER_ADMIN");
            return true;
        }

        // 🔐 Else check if user has any of the specified permissions
        Set<String> grantedAuthorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        for (String required : permissions) {

            if (grantedAuthorities.contains(required)) {
                log.info("✅ Has permission");
                return true;
            }
        }

        log.warn(Colors.YELLOW + "No permission found" + Colors.RESET);
        return false;
    }

}
