package com.flex.task_tracker.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flex.task_tracker.common.exceptions.runtime.MissingDesignationException;
import com.flex.task_tracker.common.http.HttpResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class MissingDesignationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        Throwable cause = authException.getCause();
        String message = "Unauthorized";

        if (cause instanceof MissingDesignationException) {
            message = cause.getMessage();
            response.setStatus(HttpStatus.CONFLICT.value()); // 409
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401
        }

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(
                new HttpResponse<>().responseFail(message)
        ));
    }
}
