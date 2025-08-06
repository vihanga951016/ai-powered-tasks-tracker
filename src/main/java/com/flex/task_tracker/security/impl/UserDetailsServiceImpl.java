package com.flex.task_tracker.security.impl;

import com.flex.task_tracker.app.entities.designation.DesignationPermission;
import com.flex.task_tracker.app.entities.user.User;
import com.flex.task_tracker.app.repositories.designation.DesignationPermissionRepository;
import com.flex.task_tracker.app.repositories.user.UserRepository;
import com.flex.task_tracker.app.services.designation.DesignationService;
import com.flex.task_tracker.app.services.designation.caching.DesignationCacheService;
import com.flex.task_tracker.app.services.designation.helper.DesignationServiceHelper;
import com.flex.task_tracker.common.constants.Colors;
import com.flex.task_tracker.common.exceptions.runtime.MissingDesignationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DesignationCacheService designationCacheService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDeletedIsFalse(username);

        if (user.getDesignation() == null) {
            throw new MissingDesignationException("User has no designation assigned");
        }

        //for check caching
        boolean cashed = designationCacheService.isPermissionsCached(user.getDesignation().getId());
        log.info("permissions of the designation " + user.getDesignation().getId() + " is "
                + Colors.YELLOW + (cashed ? "cashed" : "not cashed") + Colors.RESET);

        List<DesignationPermission> allPermissionsForDesignation = designationCacheService
                .getAllPermissionsForDesignation(user.getDesignation().getId());

        List<GrantedAuthority> authorities = allPermissionsForDesignation.stream()
                .map(dp -> new SimpleGrantedAuthority(dp.getPermission().getPermission()))
                .collect(Collectors.toList());

        log.info("user permissions: " + authorities);

        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}
