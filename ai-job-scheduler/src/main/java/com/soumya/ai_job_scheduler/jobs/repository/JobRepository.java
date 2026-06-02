package com.soumya.ai_job_scheduler.jobs.repository;

import com.soumya.ai_job_scheduler.jobs.entity.Job;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {
    @Query("""
            SELECT j FROM Job j 
            WHERE j.status = 'ACTIVE'
            AND j.nextRunTime <= :now
        """)
    List<Job> findRunnableJobs(@Param("now")LocalDateTime now);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT j FROM Job j
        WHERE j.id = :id
        """)
    Optional<Job> lockJob(@Param("id") UUID id);
}
