package com.soumya.ai_job_scheduler.jobs.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_execution")
@NoArgsConstructor
@Getter
@Setter
public class JobExecution {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExecutionStatus status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(columnDefinition = "TEXT")
    private String logs;

    @Column(name = "execution_duration_ms")
    private Long executionDurationMs;

    @Column(name = "worker_thread")
    private String workerThread;

    @Column(name = "attempt_number")
    private Integer attemptNumber;

}
