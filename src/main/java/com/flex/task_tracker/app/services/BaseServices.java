package com.flex.task_tracker.app.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface BaseServices<T> {

    ResponseEntity add(T data, HttpServletRequest request);

    ResponseEntity update(T data, HttpServletRequest request);

    ResponseEntity getAll(HttpServletRequest request);

    ResponseEntity getById(Integer id, HttpServletRequest request);

    ResponseEntity delete(Integer id, HttpServletRequest request);
}
