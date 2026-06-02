package com.soumya.ai_job_scheduler.common.exception;

import com.soumya.ai_job_scheduler.jobs.dto.ErrorResponse;
import com.soumya.ai_job_scheduler.jobs.exception.JobNotFoundException;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobNotFoundException.class)
    public ErrorResponse handleJobNotFoundException(JobNotFoundException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getFieldError().getDefaultMessage();
        return new ErrorResponse(errorMsg, HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(Exception e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
    }
}
