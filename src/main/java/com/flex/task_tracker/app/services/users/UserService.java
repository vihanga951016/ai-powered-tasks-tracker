package com.flex.task_tracker.app.services.users;

import com.flex.task_tracker.app.entities.User;
import com.flex.task_tracker.app.entities.http.requests.LoginRequest;
import com.flex.task_tracker.app.services.BaseServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface UserService extends BaseServices<User> {

    ResponseEntity login(LoginRequest loginRequest, HttpServletRequest request);

    ResponseEntity registerAdmin(User user, HttpServletRequest request);

    @Override
    ResponseEntity add(User data, HttpServletRequest request);

    @Override
    ResponseEntity update(User data, HttpServletRequest request);

    @Override
    ResponseEntity getAll(HttpServletRequest request);

    @Override
    ResponseEntity getById(Integer id, HttpServletRequest request);

    @Override
    ResponseEntity delete(Integer id, HttpServletRequest request);

    ResponseEntity userRestrict(Integer id, HttpServletRequest request);

    ResponseEntity logout(HttpServletRequest request);
}
