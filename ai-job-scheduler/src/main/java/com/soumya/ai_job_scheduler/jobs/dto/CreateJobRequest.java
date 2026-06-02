package com.soumya.ai_job_scheduler.jobs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateJobRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String command;
    @NotBlank
    private String cronExpression;
    @NotNull
    private Integer retryCount;

}
