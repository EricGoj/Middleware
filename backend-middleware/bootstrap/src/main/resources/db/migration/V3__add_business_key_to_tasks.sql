-- Add business_key column to tasks table
ALTER TABLE tasks 
    ADD COLUMN business_key VARCHAR(20);

-- Create an index on business_key if it's frequently queried
CREATE INDEX idx_tasks_business_key ON tasks(business_key);
