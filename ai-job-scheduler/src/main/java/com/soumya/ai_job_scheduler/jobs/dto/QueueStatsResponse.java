package com.soumya.ai_job_scheduler.jobs.dto;

import lombok.Getter;

@Getter
public class QueueStatsResponse {
    private int queuedJobs;

    private int workerCount;

    private long runningJobs;

    private int failedQueueSize;

    public QueueStatsResponse(int queuedJobs, int workerCount, long runningJobs, int failedQueueSize) {
        this.queuedJobs = queuedJobs;
        this.workerCount = workerCount;
        this.runningJobs = runningJobs;
        this.failedQueueSize = failedQueueSize;
    }
}
