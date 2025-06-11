package com.flex.task_tracker.app.controllers.user;

import com.flex.task_tracker.app.entities.User;
import com.flex.task_tracker.app.entities.http.requests.LoginRequest;
import com.flex.task_tracker.app.services.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest,
                                HttpServletRequest request) {
        return userService.login(loginRequest, request);
    }

    @PostMapping("/register")
    public ResponseEntity adminRegister(@RequestBody User user,
                                HttpServletRequest request) {
        return userService.registerAdmin(user, request);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity add(@RequestBody User data, HttpServletRequest request) {
        return userService.add(data, request);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    public ResponseEntity update(@RequestBody User data, HttpServletRequest request) {
        return userService.update(data, request);
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    public ResponseEntity getAll(HttpServletRequest request) {
        return userService.getAll(request);
    }

    @GetMapping("/{id}/get")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    public ResponseEntity getById(@PathVariable Integer id,
                                  HttpServletRequest request) {
        return userService.getById(id, request);
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity delete(@PathVariable Integer id,
                                 HttpServletRequest request) {
        return userService.delete(id, request);
    }

    @PostMapping("/{id}/restrict")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity restrict(@PathVariable Integer id,
                                HttpServletRequest request) {
        return userService.userRestrict(id, request);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','USER')")
    public ResponseEntity logout(HttpServletRequest request) {
        return userService.logout(request);
    }
}
