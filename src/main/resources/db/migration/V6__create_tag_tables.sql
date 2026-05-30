CREATE TABLE tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT uk_tag_name UNIQUE (name)
);

CREATE TABLE artifact_tag (
    artifact_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (artifact_id, tag_id),
    CONSTRAINT fk_artifact_tag_artifact FOREIGN KEY (artifact_id) REFERENCES artifact(id) ON DELETE CASCADE,
    CONSTRAINT fk_artifact_tag_tag FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
);
