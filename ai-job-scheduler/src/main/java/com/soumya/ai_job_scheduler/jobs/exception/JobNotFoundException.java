package com.soumya.ai_job_scheduler.jobs.exception;

public class JobNotFoundException extends RuntimeException {
    public  JobNotFoundException(String message) {
        super(message);
    }
}
