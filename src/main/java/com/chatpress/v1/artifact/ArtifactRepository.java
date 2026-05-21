package com.chatpress.v1.artifact;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtifactRepository extends JpaRepository<Artifact, Long> {

    Optional<Artifact> findBySlug(String slug);
}
