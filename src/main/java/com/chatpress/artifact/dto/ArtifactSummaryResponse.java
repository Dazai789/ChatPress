package com.chatpress.artifact.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.chatpress.artifact.Artifact;

public record ArtifactSummaryResponse(
        Long id,
        String title,
        String slug,
        String sourceFormat,
        String status,
        List<String> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ArtifactSummaryResponse from(Artifact artifact) {
        List<String> tagNames = artifact.getTags().stream()
                .map(tag -> tag.getName())
                .toList();
        return new ArtifactSummaryResponse(
                artifact.getId(),
                artifact.getTitle(),
                artifact.getSlug(),
                artifact.getSourceFormat(),
                artifact.getStatus().name().toLowerCase(),
                tagNames,
                artifact.getCreatedAt(),
                artifact.getUpdatedAt()
        );
    }
}
