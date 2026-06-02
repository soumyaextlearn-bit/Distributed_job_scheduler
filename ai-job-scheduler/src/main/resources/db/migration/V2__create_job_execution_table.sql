CREATE TABLE job_execution (

                               id UUID PRIMARY KEY,

                               job_id UUID NOT NULL,

                               status VARCHAR(50) NOT NULL,

                               started_at TIMESTAMP NOT NULL,

                               completed_at TIMESTAMP,

                               logs TEXT,

                               FOREIGN KEY (job_id) REFERENCES jobs(id)
);