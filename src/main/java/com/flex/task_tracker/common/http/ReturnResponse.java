package com.flex.task_tracker.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@SuppressWarnings("Duplicates")
public class ReturnResponse {
    public static ResponseEntity BAD_REQUEST(String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new HttpResponse<>().responseFail(message));
    }

    public static ResponseEntity ERROR(String message) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new HttpResponse<>().responseFail(message));
    }

    public static ResponseEntity SUCCESS(String message) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new HttpResponse<>().responseOk(message));
    }

    public static<T> ResponseEntity DATA(T data) {
        return ResponseEntity.ok().body(new HttpResponse<>()
                .responseOk(data));
    }

    public static ResponseEntity UNAUTHORIZED(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new HttpResponse<>().responseFail(message));
    }

    public static void FORBIDDEN(ObjectMapper objectMapper, HttpServletResponse response) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            HttpResponse<Object> body = new HttpResponse<>()
                    .responseFail("You are not allowed to access this resource");

            String json = objectMapper.writeValueAsString(body);
            response.getWriter().write(json);
        }
    }
}
