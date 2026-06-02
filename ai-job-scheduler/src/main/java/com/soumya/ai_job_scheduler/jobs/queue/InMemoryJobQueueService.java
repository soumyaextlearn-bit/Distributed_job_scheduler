package com.soumya.ai_job_scheduler.jobs.queue;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class InMemoryJobQueueService implements JobQueueService {
    private final BlockingQueue<UUID> queue = new LinkedBlockingQueue<>();
    @Override
    public void enqueue(UUID jobId) {
        queue.offer(jobId);
        System.out.println("Queued job : " + jobId);
    }

    @Override
    public UUID dequeue() throws InterruptedException {
        return queue.take();
    }
}
