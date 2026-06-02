package com.soumya.ai_job_scheduler.jobs.dto;

import com.soumya.ai_job_scheduler.jobs.entity.ExecutionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
public class ExecutionResponse {
    private UUID id;
    private ExecutionStatus status;
    private Long executionDurationMs;
    private String workerThread;
    private Integer attemptNumber;
    private String logs;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
