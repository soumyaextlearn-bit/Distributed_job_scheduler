package com.soumya.ai_job_scheduler.jobs.worker;

import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.queue.JobQueueService;
import com.soumya.ai_job_scheduler.jobs.repository.JobRepository;
import com.soumya.ai_job_scheduler.jobs.service.JobExecutionService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobWorkerService {
    private final JobQueueService jobQueueService;

    private final JobRepository jobRepository;

    private final JobExecutionService jobExecutionService;

    public JobWorkerService(JobQueueService jobQueueService, JobRepository jobRepository, JobExecutionService jobExecutionService) {
        this.jobQueueService = jobQueueService;
        this.jobRepository = jobRepository;
        this.jobExecutionService = jobExecutionService;
    }
    @PostConstruct
    public void startWorker(){

        Thread worker = new Thread(() -> {
            System.out.println("worker started : " + Thread.currentThread().getName());
            while (true) {
                try {
                    UUID jobId = jobQueueService.dequeue();
                    jobExecutionService.executeJob(jobId);
                }
                catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    break;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        worker.setName("scheduler-worker-1");
        worker.setDaemon(true);
        worker.start();
    }
}
