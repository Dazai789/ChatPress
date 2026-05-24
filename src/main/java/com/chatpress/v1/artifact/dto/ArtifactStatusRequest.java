package com.chatpress.v1.artifact.dto;

import com.chatpress.v1.artifact.Artifact;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Locale;

public record ArtifactStatusRequest(
        @NotBlank
        @Pattern(regexp = "draft|published")
        String status
) {

    public Artifact.Status toArtifactStatus() {
        return Artifact.Status.valueOf(status.toUpperCase(Locale.ROOT));
    }
}
