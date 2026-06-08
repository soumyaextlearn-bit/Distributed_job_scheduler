package com.soumya.ai_job_scheduler.jobs.worker;

import com.soumya.ai_job_scheduler.jobs.queue.JobQueueService;
import com.soumya.ai_job_scheduler.jobs.service.JobExecutionService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class JobWorkerService {
    private final JobQueueService jobQueueService;
    @Value("${scheduler.worker.count}")
    private int workerCount;

    private final JobExecutionService jobExecutionService;

    private final List<Thread>  workers = new ArrayList<>();

    private volatile boolean running = true;

    public JobWorkerService(JobQueueService jobQueueService, JobExecutionService jobExecutionService) {
        this.jobQueueService = jobQueueService;
        this.jobExecutionService = jobExecutionService;
    }
    @PostConstruct
    public void startWorker(){
        for(int i = 1; i <= workerCount; i++){
            int workerId = i;
            Thread worker = new Thread(() -> {
                System.out.println("worker started : " + Thread.currentThread().getName());
                while (running) {
                    try {
                        UUID jobId = jobQueueService.dequeue();
                        jobExecutionService.executeJob(jobId);
                    }
                    catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                        break;
                    }
                    catch (Exception e){
                        if(!running){
                            break;
                        }
                        e.printStackTrace();
                    }
                }
            });
            worker.setName("scheduler-worker-"+workerId);
            worker.setDaemon(true);
            worker.start();
            workers.add(worker);
        }

    }
    @PreDestroy
    public void shutdownWorker(){
        System.out.println("Stopping workers... ");
        running = false;
        workers.forEach(Thread::interrupt);
    }
}
