package com.soumya.ai_job_scheduler.jobs.executor;

import lombok.Getter;

@Getter
public class ExecutionResult {
    private final boolean success;
    private final String logs;
    public ExecutionResult(boolean success, String logs) {
        this.success = success;
        this.logs = logs;
    }


}
