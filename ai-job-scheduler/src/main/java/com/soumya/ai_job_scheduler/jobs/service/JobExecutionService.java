package com.soumya.ai_job_scheduler.jobs.service;

import com.soumya.ai_job_scheduler.jobs.dto.ExecutionResponse;
import com.soumya.ai_job_scheduler.jobs.entity.ExecutionStatus;
import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.entity.JobExecution;
import com.soumya.ai_job_scheduler.jobs.entity.JobStatus;
import com.soumya.ai_job_scheduler.jobs.executor.CommandExecutor;
import com.soumya.ai_job_scheduler.jobs.executor.ExecutionResult;
import com.soumya.ai_job_scheduler.jobs.queue.JobQueueService;
import com.soumya.ai_job_scheduler.jobs.repository.JobExecutionRepository;
import com.soumya.ai_job_scheduler.jobs.repository.JobRepository;
import com.soumya.ai_job_scheduler.jobs.util.CronUtils;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobExecutionService {

    private final JobExecutionRepository jobExecutionRepository;
    private final JobRepository jobRepository;
    private final CommandExecutor commandExecutor;
    private final JobQueueService jobQueueService;

    public JobExecutionService(
            JobExecutionRepository jobExecutionRepository,
            JobRepository jobRepository,
            CommandExecutor commandExecutor,
            JobQueueService jobQueueService
    ){
        this.jobExecutionRepository = jobExecutionRepository;
        this.jobRepository = jobRepository;
        this.commandExecutor = commandExecutor;
        this.jobQueueService = jobQueueService;
    }
    public List<ExecutionResponse> getExecutions(UUID jobId) {
        return jobExecutionRepository
                .findByJobId(jobId)
                .stream()
                .map(this::mapExecution)
                .toList();
    }
    private ExecutionResponse mapExecution(JobExecution jobExecution) {
        ExecutionResponse executionResponse = new ExecutionResponse();
        executionResponse.setId(jobExecution.getId());
        executionResponse.setStatus(jobExecution.getStatus());
        executionResponse.setExecutionDurationMs(jobExecution.getExecutionDurationMs());
        executionResponse.setWorkerThread(jobExecution.getWorkerThread());
        executionResponse.setAttemptNumber(jobExecution.getAttemptNumber());
        executionResponse.setLogs(jobExecution.getLogs());
        executionResponse.setStartedAt(jobExecution.getStartedAt());
        executionResponse.setCompletedAt(jobExecution.getCompletedAt());
        return executionResponse;
    }
    private void handleJobFailure(Job job){

        int retries = job.getCurrentRetryCount();

        if(retries < job.getRetryCount()){
            job.setCurrentRetryCount(retries+1);
            System.out.println("Retry Count : " + job.getCurrentRetryCount());
            job.setStatus(JobStatus.ACTIVE);
        }else{
            job.setStatus(JobStatus.FAILED);
            System.out.println("adding to the failed queue");
            jobQueueService.enqueueFailed(job.getId());
        }
    }

    public void executeJob(UUID jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow();
        JobExecution jobExecution = new JobExecution();
        jobExecution.setJob(job);
        jobExecution.setId(UUID.randomUUID());
        jobExecution.setStatus(ExecutionStatus.RUNNING);
        jobExecution.setStartedAt(LocalDateTime.now());
        jobExecution.setWorkerThread(Thread.currentThread().getName());
        jobExecution.setAttemptNumber(job.getCurrentRetryCount()+1);
        jobExecutionRepository.save(jobExecution);

        try{
            System.out.println("executing job " + job.getName());
            ExecutionResult result = commandExecutor.execute(job.getCommand());
            
            if(result.isSuccess()){
                jobExecution.setStatus(ExecutionStatus.SUCCESS);
                job.setCurrentRetryCount(0);
                job.setStatus(JobStatus.ACTIVE);
            }else {
                jobExecution.setStatus(ExecutionStatus.FAILED);
                handleJobFailure(job);
            }
            jobExecution.setLogs(result.getLogs());
        } catch (Exception e) {
            jobExecution.setStatus(ExecutionStatus.FAILED);
            jobExecution.setLogs(e.getMessage());
            handleJobFailure(job);
        }


        System.out.println(
                "Job Finished : "
                        + job.getName()
                        + " Status : "
                        + jobExecution.getStatus()
        );
        Duration jobExecutionDuration =  Duration.between(jobExecution.getStartedAt(), LocalDateTime.now());
        jobExecution.setExecutionDurationMs(jobExecutionDuration.toMillis());
        jobExecution.setCompletedAt(LocalDateTime.now());
        jobExecutionRepository.save(jobExecution);

        if(job.getStatus() == JobStatus.ACTIVE) {
            job.setNextRunTime(
                    CronUtils.calculateNextRunTime(
                            job.getCronExpression()
                    )
            );
        }

        jobRepository.save(job);
    }
}
