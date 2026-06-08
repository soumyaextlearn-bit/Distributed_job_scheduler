package com.soumya.ai_job_scheduler.jobs.controller;

import com.soumya.ai_job_scheduler.jobs.dto.FailedQueueResponse;
import com.soumya.ai_job_scheduler.jobs.dto.QueueStatsResponse;
import com.soumya.ai_job_scheduler.jobs.queue.QueueStatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/queue")
public class QueueController {
    private final QueueStatsService queueStatsService;

    public QueueController(QueueStatsService queueStatsService) {
        this.queueStatsService = queueStatsService;
    }

    @GetMapping("/stats")
    public QueueStatsResponse getStats(){
        return queueStatsService.getStats();
    }
    @GetMapping("/failed")
    public FailedQueueResponse getFailedQueue(){
        return queueStatsService.getFailedJobs();
    }
    @PostMapping("/failed/replay")
    public ResponseEntity<String> replayFailedJobs(){
        queueStatsService.replayFailedJobs();
        return ResponseEntity.ok("Replay Triggered.");
    }
}
