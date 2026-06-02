package com.soumya.ai_job_scheduler.jobs.executor;

import com.soumya.ai_job_scheduler.jobs.entity.Job;
import com.soumya.ai_job_scheduler.jobs.service.JobExecutionService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncJobExecutor {
   private final JobExecutionService jobExecutionService;

   public AsyncJobExecutor(
           JobExecutionService jobExecutionService
   ) {
       this.jobExecutionService = jobExecutionService;
   }
   @Async("jobExecutor")
   public void execute(Job job) {
       System.out.println(Thread.currentThread().getName());
       jobExecutionService.executeJob(job);
   }
}
