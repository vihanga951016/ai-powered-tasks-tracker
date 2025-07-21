package com.flex.task_tracker.common.exceptions;

import com.flex.task_tracker.common.constants.Colors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.flex.task_tracker.common.http.ReturnResponse.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(Colors.YELLOW + ex.getRootCause().getMessage() + Colors.RESET);
        ex.printStackTrace();
        return SERVER_ERROR("Data conflict");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleAllExceptions(Exception ex) {
        log.error(Colors.YELLOW + ex.getMessage() + Colors.RESET);
        ex.printStackTrace();
        return SERVER_ERROR("An unexpected error occurred");
    }
}
