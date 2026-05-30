CREATE TABLE artifact (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    slug VARCHAR(200) NOT NULL,
    source_format VARCHAR(50) NOT NULL,
    source_content TEXT NOT NULL,
    rendered_html TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    CONSTRAINT uk_artifact_slug UNIQUE (slug)
);
