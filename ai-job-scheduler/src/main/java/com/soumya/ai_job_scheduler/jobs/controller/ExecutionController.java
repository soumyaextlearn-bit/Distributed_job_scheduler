package com.soumya.ai_job_scheduler.jobs.controller;

import com.soumya.ai_job_scheduler.jobs.dto.ExecutionResponse;
import com.soumya.ai_job_scheduler.jobs.service.JobExecutionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/executions")
public class ExecutionController {
    private final JobExecutionService jobExecutionService;
    public ExecutionController(JobExecutionService jobExecutionService) {
        this.jobExecutionService = jobExecutionService;
    }
    @GetMapping("/{jobId}")
    public List<ExecutionResponse> getExecutions(@PathVariable UUID jobId) {
        return jobExecutionService.getExecutions(jobId);
    }
}
