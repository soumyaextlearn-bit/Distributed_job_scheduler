package com.soumya.ai_job_scheduler.jobs.dto;

import com.soumya.ai_job_scheduler.jobs.entity.JobStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class JobResponse {

    private UUID id;
    private String name;
    private String command;
    private String cronExpression;
    private JobStatus status;
    private Integer retryCount;
    private LocalDateTime createdAt;
}
