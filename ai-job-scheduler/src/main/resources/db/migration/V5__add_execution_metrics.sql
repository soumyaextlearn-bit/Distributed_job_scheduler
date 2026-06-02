ALTER TABLE job_execution
    ADD COLUMN execution_duration_ms BIGINT;

ALTER TABLE job_execution
    ADD COLUMN worker_thread VARCHAR(100);

ALTER TABLE job_execution
    ADD COLUMN attempt_number INT;