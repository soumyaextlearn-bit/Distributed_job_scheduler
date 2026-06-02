package com.soumya.ai_job_scheduler.jobs.scheduler;

import com.soumya.ai_job_scheduler.jobs.entity.ExecutionStatus;
import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.entity.JobExecution;
import com.soumya.ai_job_scheduler.jobs.entity.JobStatus;
import com.soumya.ai_job_scheduler.jobs.executor.AsyncJobExcutor;
import com.soumya.ai_job_scheduler.jobs.executor.CommandExecutor;
import com.soumya.ai_job_scheduler.jobs.executor.ExecutionResult;
import com.soumya.ai_job_scheduler.jobs.repository.JobExecutionRepository;
import com.soumya.ai_job_scheduler.jobs.repository.JobRepository;
import com.soumya.ai_job_scheduler.jobs.util.CronUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobSchedulerService {

    private final JobRepository jobRepository;
    private final AsyncJobExcutor asyncJobExcutor;

    public JobSchedulerService(
            JobRepository jobRepository,
            JobExecutionRepository jobExecutionRepository,
            CommandExecutor commandExecutor,
            AsyncJobExcutor asyncJobExcutor
    )
    {
        this.jobRepository = jobRepository;
        this.asyncJobExcutor = asyncJobExcutor;
    }
    @Scheduled(fixedRate = 30000)
    public void pollAndExcuteJob(){
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
