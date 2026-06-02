package com.soumya.ai_job_scheduler.jobs.executor;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
@Component
public class CommandExecutor {

    public ExecutionResult execute(String command) {
        try{
            ProcessBuilder processBuilder = buildProcess(command);
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            return new ExecutionResult(exitCode == 0, output.toString());
        }catch (Exception e){
            return new ExecutionResult(
                    false,
                    e.getMessage()
            );
        }
    }

    private ProcessBuilder buildProcess(String command) {
        String os = System.getProperty("os.name").toLowerCase();

        ProcessBuilder processBuilder = new ProcessBuilder();
        if(os.contains("win")) {
            processBuilder.command("cmd.exe", "/c", command);
        }else {
            processBuilder.command("sh", "-c", command);
        }
        return processBuilder;
    }
}
