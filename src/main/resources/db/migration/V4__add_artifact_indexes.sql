CREATE INDEX idx_artifact_created_by_created_at ON artifact (created_by, created_at);
CREATE INDEX idx_artifact_status_created_at ON artifact (status, created_at);
