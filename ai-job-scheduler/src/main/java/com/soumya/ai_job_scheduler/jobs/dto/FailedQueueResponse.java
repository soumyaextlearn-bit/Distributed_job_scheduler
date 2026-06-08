package com.soumya.ai_job_scheduler.jobs.dto;

import lombok.Getter;

import java.util.List;
import java.util.UUID;
@Getter
public class FailedQueueResponse {
    private List<UUID> failedJobIds;

    public FailedQueueResponse(List<UUID> failedJobIds) {
        this.failedJobIds = failedJobIds;
    }

}
