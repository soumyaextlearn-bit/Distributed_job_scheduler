package com.soumya.ai_job_scheduler.jobs.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "jobs")
public class Job {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "command", nullable = false)
    private String command;

    @Column(name = "cron_expression", nullable = false)
    private String cronExpression;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "next_run_time")
    private LocalDateTime nextRunTime;

    @Column(name = "current_retry_count")
    private Integer currentRetryCount;
}
