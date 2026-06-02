package com.soumya.ai_job_scheduler.jobs.service;

import com.soumya.ai_job_scheduler.jobs.dto.CreateJobRequest;
import com.soumya.ai_job_scheduler.jobs.dto.JobResponse;
import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.entity.JobStatus;
import com.soumya.ai_job_scheduler.jobs.exception.JobNotFoundException;
import com.soumya.ai_job_scheduler.jobs.repository.JobRepository;
import com.soumya.ai_job_scheduler.jobs.util.CronUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobResponse createJob(CreateJobRequest request) {
        Job job = new Job();
        job.setId(UUID.randomUUID());
        job.setName(request.getName());
        job.setCommand(request.getCommand());
        job.setCronExpression(request.getCronExpression());
        job.setRetryCount(request.getRetryCount());

        job.setStatus(JobStatus.ACTIVE);
        job.setCreatedAt(LocalDateTime.now());
        job.setNextRunTime(
                CronUtils.calculateNextRunTime(request.getCronExpression())
        );
        job.setCurrentRetryCount(0);
        Job savedJob = jobRepository.save(job);
        return mapToResponse(savedJob);

    }

    public List<JobResponse> getAllJobs() {
       return jobRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public JobResponse getJobById(UUID id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job Not Found"));
        return mapToResponse(job);
    }

    public void deleteJobById(UUID id) {
        jobRepository.deleteById(id);
    }

    private  JobResponse mapToResponse(Job job) {
        JobResponse jobResponse = new JobResponse();
        jobResponse.setId(job.getId());
        jobResponse.setName(job.getName());
        jobResponse.setCronExpression(job.getCronExpression());
        jobResponse.setCommand(job.getCommand());
        jobResponse.setStatus(job.getStatus());
        jobResponse.setCreatedAt(job.getCreatedAt());
        jobResponse.setRetryCount(job.getRetryCount());

        return jobResponse;
    }
}
