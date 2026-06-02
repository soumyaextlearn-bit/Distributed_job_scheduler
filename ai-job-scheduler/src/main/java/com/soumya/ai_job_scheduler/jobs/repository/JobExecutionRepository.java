package com.soumya.ai_job_scheduler.jobs.repository;

import com.soumya.ai_job_scheduler.jobs.entity.JobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {
    List<JobExecution> findByJobId(UUID jobId);
}
