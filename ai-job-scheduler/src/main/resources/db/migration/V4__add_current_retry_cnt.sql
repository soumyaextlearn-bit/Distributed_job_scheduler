ALTER TABLE jobs
    ADD COLUMN current_retry_count INT DEFAULT 0;