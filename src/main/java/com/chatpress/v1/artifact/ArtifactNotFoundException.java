package com.chatpress.v1.artifact;

public class ArtifactNotFoundException extends RuntimeException {

    public ArtifactNotFoundException(Long id) {
        super("Artifact not found: " + id);
    }
}
