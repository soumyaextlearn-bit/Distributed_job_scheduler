package com.soumya.ai_job_scheduler.jobs.repository;

import com.soumya.ai_job_scheduler.jobs.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
}
