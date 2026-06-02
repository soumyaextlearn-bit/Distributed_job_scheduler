CREATE TABLE jobs (
                      id UUID PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      command TEXT NOT NULL,
                      cron_expression VARCHAR(100) NOT NULL,
                      status VARCHAR(50) NOT NULL,
                      retry_count INT DEFAULT 0,
                      created_at TIMESTAMP NOT NULL
);