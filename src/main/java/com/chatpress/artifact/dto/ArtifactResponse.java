package com.chatpress.artifact.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.chatpress.artifact.Artifact;

public record ArtifactResponse(
        Long id,
        String title,
        String slug,
        String sourceFormat,
        String sourceContent,
        String renderedHtml,
        String status,
        List<String> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ArtifactResponse from(Artifact artifact) {
        List<String> tagNames = artifact.getTags().stream()
                .map(tag -> tag.getName())
                .toList();
        return new ArtifactResponse(
                artifact.getId(),
                artifact.getTitle(),
                artifact.getSlug(),
                artifact.getSourceFormat(),
                artifact.getSourceContent(),
                artifact.getRenderedHtml(),
                artifact.getStatus().name().toLowerCase(),
                tagNames,
                artifact.getCreatedAt(),
                artifact.getUpdatedAt()
        );
    }
}
