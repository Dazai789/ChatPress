CREATE TABLE operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    action VARCHAR(100) NOT NULL,
    target VARCHAR(255) NOT NULL,
    duration_ms BIGINT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL
);
