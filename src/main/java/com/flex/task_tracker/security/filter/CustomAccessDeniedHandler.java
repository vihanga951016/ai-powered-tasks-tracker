package com.flex.task_tracker.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flex.task_tracker.common.http.HttpResponse;
import com.flex.task_tracker.common.http.ReturnResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@SuppressWarnings("Duplicates")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ReturnResponse.FORBIDDEN(objectMapper, response);
    }
}
