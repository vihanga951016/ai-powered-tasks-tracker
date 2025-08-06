package com.flex.task_tracker.app.services.users.impls;

import com.flex.task_tracker.app.entities.user.User;
import com.flex.task_tracker.app.entities.user.UserLogin;
import com.flex.task_tracker.app.entities.user.requests.LoginRequest;
import com.flex.task_tracker.app.repositories.user.UserLoginRepository;
import com.flex.task_tracker.app.repositories.user.UserRepository;
import com.flex.task_tracker.app.services.designation.caching.DesignationCacheService;
import com.flex.task_tracker.app.services.users.UserService;
import com.flex.task_tracker.security.utils.HashUtil;
import com.flex.task_tracker.security.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.flex.task_tracker.common.http.ReturnResponse.*;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("Duplicates")
public class UserServiceImpl implements UserService {

    private final UserHelper userHelper;
    private final DesignationCacheService designationCacheService;

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;

    private final JwtUtil jwtUtil;

    @Override
    public ResponseEntity login(LoginRequest loginRequest, HttpServletRequest request) {
        log.info(request.getRequestURI());

        User existingUser = userRepository.findByEmailAndDeletedIsFalse(loginRequest.getEmail());

        if (existingUser == null) {
            log.info(loginRequest.getEmail());
            return ERROR("Wrong Email");
        }

        if (existingUser.getDesignation() == null) {
            return ERROR("Designation not found");
        }

        //remove designation permission cache
        designationCacheService.evictPermissionsCache(existingUser.getDesignation().getId());

        if (!HashUtil.checkEncrypted(loginRequest.getPassword(), existingUser.getPassword())) {
            log.info(loginRequest.getPassword());
            return ERROR("Wrong Password");
        }

        userHelper.logoutPreviousLogins(existingUser.getId());

        String token = jwtUtil.generateToken(existingUser);

        UserLogin userLogin = UserLogin.builder()
                .user(existingUser)
                .token(token)
                .loginTime(new Date())
                .build();

        userLoginRepository.save(userLogin);

        return DATA(token);
    }

    @Override
    public ResponseEntity registerAdmin(User user, HttpServletRequest request) {
        log.info(request.getRequestURI());

        if (userRepository.onlyOneAdmin() != null)
            return ERROR("Already has an admin");

        if (userRepository.existsByEmailAndDeletedIsFalse(user.getEmail()))
            return ERROR("User already registered");

        user.setPassword(HashUtil.hash(user.getPassword()));

        userRepository.save(user);

        return SUCCESS("User Registered");
    }

    @Override
    public ResponseEntity add(User data, HttpServletRequest request) {
        log.info(request.getRequestURI());
        if (userRepository.existsByEmailAndDeletedIsFalse(data.getEmail()))
            return ERROR("User already registered");

        data.setPassword(HashUtil.hash(data.getPassword()));
        data.setRole("USER");

        userRepository.save(data);

        return SUCCESS("User Added");
    }

    @Override
    public ResponseEntity update(User data, HttpServletRequest request) {
        log.info(request.getRequestURI());
        if (data.getId() == null) return BAD_REQUEST("User id not found");

        User user = userRepository.findByIdAndDeletedIsFalse(data.getId());

        if (user == null) {
            log.info("userId: " + data.getId());
            return ERROR("User not found");
        }

        if (!userHelper.authorizingUser(user, request)) return UNAUTHORIZED("You are not allowed to do this");

        User updatedUser = userHelper.updatingUserData(user, data);

        userRepository.save(updatedUser);

        return SUCCESS("User updated");
    }

    @Override
    public ResponseEntity getAll(HttpServletRequest request) {
        log.info(request.getRequestURI());

        return DATA(userRepository.findAll());
    }

    @Override
    public ResponseEntity getById(Integer id, HttpServletRequest request) {
        log.info(request.getRequestURI());

        User user = userRepository.findByIdAndDeletedIsFalse(id);

        if (user == null) {
            log.info("userId: " + id);
            return ERROR("User not found");
        }

        return DATA(user);
    }

    @Override
    public ResponseEntity delete(Integer id, HttpServletRequest request) {
        log.info(request.getRequestURI());

        User user = userRepository.findByIdAndDeletedIsFalse(id);

        if (user == null) {
            log.info("userId: " + id);
            return ERROR("User not found");
        }

        if (user.getRole().equals("SUPER_ADMIN")) return ERROR("Can not delete the super admin");

        user.setDeleted(true);

        userRepository.save(user);

        return DATA(user.getFirstName() + " deleted");
    }

    @Override
    public ResponseEntity userRestrict(Integer id, HttpServletRequest request) {
        log.info(request.getRequestURI());

        User user = userRepository.findByIdAndDeletedIsFalse(id);

        if (user == null) {
            log.info("userId: " + id);
            return ERROR("User not found");
        }

        if (user.getRole().equals("SUPER_ADMIN")) return ERROR("Can not restricted the super admin");

        user.setRestricted(!user.isRestricted());

        userRepository.save(user);

        if (user.isRestricted()) {
            return DATA(user.getFirstName() + " Restricted");
        } else {
            return DATA(user.getFirstName() + " Activated");
        }
    }

    @Override
    public ResponseEntity logout(HttpServletRequest request) {
        String email = JwtUtil.extractUsername(request);

        if (email == null) return ERROR("Invalid email");

        User user = userRepository.findByEmailAndDeletedIsFalse(email);

        if (user == null)  return ERROR("User not found from " + email);

        userHelper.logoutPreviousLogins(user.getId());
        designationCacheService.evictPermissionsCache(user.getDesignation().getId());

        return SUCCESS("User successfully logout");
    }
}
