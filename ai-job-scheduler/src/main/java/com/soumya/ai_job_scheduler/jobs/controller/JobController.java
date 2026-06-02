package com.soumya.ai_job_scheduler.jobs.controller;

import com.soumya.ai_job_scheduler.jobs.dto.CreateJobRequest;
import com.soumya.ai_job_scheduler.jobs.dto.JobResponse;
import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@Valid @RequestBody CreateJobRequest request) {
        return jobService.createJob(request);
    }

    @GetMapping
    public List<JobResponse> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable UUID id) {
        return jobService.getJobById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJobById(@PathVariable UUID id) {
        jobService.deleteJobById(id);
    }
}

