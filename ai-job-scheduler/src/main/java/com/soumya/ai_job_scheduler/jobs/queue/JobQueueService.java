package com.soumya.ai_job_scheduler.jobs.queue;

import java.util.UUID;

public interface JobQueueService {
    void enqueue(UUID jobId);

    UUID dequeue() throws InterruptedException;
}
