package com.soumya.ai_job_scheduler.jobs.scheduler;

import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.entity.JobStatus;
import com.soumya.ai_job_scheduler.jobs.queue.JobQueueService;
import com.soumya.ai_job_scheduler.jobs.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobSchedulerService {

    private final JobRepository jobRepository;
    private final JobQueueService jobQueueService;

    public JobSchedulerService(
            JobRepository jobRepository,
            JobQueueService jobQueueService
    )
    {
        this.jobRepository = jobRepository;
        this.jobQueueService = jobQueueService;
    }
    @Scheduled(fixedRateString = "${scheduler.poll-interval}")
    public void pollAndExecuteJob(){
        System.out.println("Scheduler polling jobs");

        List<Job> runnableJobs = jobRepository.findRunnableJobs(LocalDateTime.now());

        for(Job job : runnableJobs){
            job.setStatus(JobStatus.RUNNING);
            jobRepository.save(job);
            jobQueueService.enqueue(job.getId());
        }
    }


}
