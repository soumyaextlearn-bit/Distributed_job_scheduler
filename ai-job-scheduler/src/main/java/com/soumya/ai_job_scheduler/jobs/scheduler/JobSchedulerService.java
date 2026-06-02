package com.soumya.ai_job_scheduler.jobs.scheduler;

import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.entity.JobStatus;
import com.soumya.ai_job_scheduler.jobs.executor.AsyncJobExecutor;
import com.soumya.ai_job_scheduler.jobs.executor.CommandExecutor;
import com.soumya.ai_job_scheduler.jobs.repository.JobExecutionRepository;
import com.soumya.ai_job_scheduler.jobs.repository.JobRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobSchedulerService {

    private final JobRepository jobRepository;
    private final AsyncJobExecutor asyncJobExcutor;

    public JobSchedulerService(
            JobRepository jobRepository,
            JobExecutionRepository jobExecutionRepository,
            CommandExecutor commandExecutor,
            AsyncJobExecutor asyncJobExcutor
    )
    {
        this.jobRepository = jobRepository;
        this.asyncJobExcutor = asyncJobExcutor;
    }
    @Scheduled(fixedRate = 30000)
    public void pollAndExecuteJob(){
        System.out.println("Scheduler polling jobs");

        List< Job> activeJobs = jobRepository.findAll()
                .stream()
                .filter(job -> job.getStatus() == JobStatus.ACTIVE
                &&
                job.getNextRunTime() != null
                &&
                !job.getNextRunTime().isAfter(LocalDateTime.now()))
                .toList();

        for(Job job : activeJobs){
            job.setStatus(JobStatus.RUNNING);
            jobRepository.save(job);
            asyncJobExcutor.execute(job);
        }
    }


}
