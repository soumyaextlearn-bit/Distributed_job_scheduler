package com.soumya.ai_job_scheduler.jobs.service;

import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.entity.JobStatus;
import com.soumya.ai_job_scheduler.jobs.repository.JobRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class JobRecoveryService {
    private final JobRepository jobRepository;

    public JobRecoveryService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }
    @PostConstruct
    public void recoverRunningJobs() {

        List<Job> runningJobs = jobRepository.findAllByStatus(JobStatus.RUNNING);
        if(runningJobs.isEmpty()) {
            System.out.println("No running jobs found for recovery");
            return;
        }
        System.out.println("Started recovering running jobs");
        for (Job job : runningJobs) {
            System.out.println("Recovering Job : "+ job.getId());
            job.setStatus(JobStatus.ACTIVE);
            jobRepository.save(job);
        }
        System.out.println("Running job recovery successfull");

    }
}
