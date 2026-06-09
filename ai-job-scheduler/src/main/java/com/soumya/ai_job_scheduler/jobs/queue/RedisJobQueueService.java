package com.soumya.ai_job_scheduler.jobs.queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class RedisJobQueueService implements JobQueueService {

    private static final String QUEUE_NAME = "job_queue";

    private static final String FAILED_QUEUE = "failed_job_queue";

    private final StringRedisTemplate redisTemplate;

    @Value("${scheduler.redis.dequeue-timeout}")
    private int dequeueDelaySeconds;

    public RedisJobQueueService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void enqueue(UUID jobId) {
        redisTemplate.opsForList().rightPush(QUEUE_NAME, jobId.toString());
        System.out.println("Queueing job id " + jobId.toString());
    }

    @Override
    public void enqueueFailed(UUID jobID) {
        redisTemplate.opsForList().leftPush(FAILED_QUEUE, jobID.toString());
        System.out.println("Queueing Failed job id " + jobID.toString());
    }

    @Override
    public UUID dequeue() throws InterruptedException {
        String jobId = redisTemplate.opsForList().rightPop(QUEUE_NAME, Duration.ofSeconds(dequeueDelaySeconds));
        if(jobId == null) {
            return null;
        }
        return UUID.fromString(jobId);
    }

    @Override
    public int getQueueSize() {
        Long size = redisTemplate.opsForList().size(QUEUE_NAME);
        return size == null ? 0 : size.intValue();
    }

    @Override
    public int getFailedQueueSize() {
        Long size = redisTemplate.opsForList().size(FAILED_QUEUE);
        return size == null ? 0 : size.intValue();
    }

    @Override
    public List<UUID> getFailedJobs() {
        List<String> failedJobs = redisTemplate.opsForList().range(FAILED_QUEUE, 0, -1);
        if(failedJobs == null) {
            return List.of();
        }

        return failedJobs.stream().map(UUID::fromString).toList();
    }

    @Override
    public void clearFailedJobs() {
        redisTemplate.opsForList().trim(FAILED_QUEUE, 1,0);
    }
}
