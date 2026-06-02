package com.soumya.ai_job_scheduler.jobs.util;

import org.springframework.scheduling.support.CronExpression;

import java.time.LocalDateTime;

public class CronUtils {
    public static LocalDateTime calculateNextRunTime(String cronExpression) {
        CronExpression expression = CronExpression.parse(cronExpression);
        return expression.next(LocalDateTime.now());
    }
}
