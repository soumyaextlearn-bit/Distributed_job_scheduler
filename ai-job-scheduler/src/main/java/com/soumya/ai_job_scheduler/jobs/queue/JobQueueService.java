package com.soumya.ai_job_scheduler.jobs.queue;

import java.util.List;
import java.util.UUID;

public interface JobQueueService {
    void enqueue(UUID jobId);

    void enqueueFailed(UUID jobID);

    UUID dequeue() throws InterruptedException;

    int getQueueSize();

    int getFailedQueueSize();

    List<UUID> getFailedJobs();

    void clearFailedJobs();
}
