package com.soumya.ai_job_scheduler.jobs.queue;

import com.soumya.ai_job_scheduler.jobs.dto.FailedQueueResponse;
import com.soumya.ai_job_scheduler.jobs.dto.QueueStatsResponse;
import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.entity.JobStatus;
import com.soumya.ai_job_scheduler.jobs.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class QueueStatsService {

    private final JobQueueService jobQueueService;
    private final JobRepository jobRepository;

    @Value("${scheduler.worker.count}")
    private int workerCount;

    public QueueStatsService(JobQueueService jobQueueService, JobRepository jobRepository) {
        this.jobQueueService = jobQueueService;
        this.jobRepository = jobRepository;
    }

    public QueueStatsResponse getStats(){
        long runningJobs = jobRepository.countByStatus(JobStatus.RUNNING);

        return new QueueStatsResponse(
                jobQueueService.getQueueSize(),
                workerCount,
                runningJobs,
                jobQueueService.getFailedQueueSize()
        );
    }

    public FailedQueueResponse getFailedJobs(){
        return new FailedQueueResponse(
                jobQueueService.getFailedJobs()
        );
    }
    @Transactional
    public void replayFailedJobs(){
        List<UUID> failedJobs = jobQueueService.getFailedJobs();

        for(UUID id : failedJobs){
            Job job = jobRepository.findById(id).orElseThrow();
            job.setStatus(JobStatus.ACTIVE);
            job.setCurrentRetryCount(0);
            jobRepository.save(job);
        }
        jobQueueService.replayFailedJobs();
    }
}
