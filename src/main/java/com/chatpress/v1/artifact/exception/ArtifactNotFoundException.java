package com.chatpress.v1.artifact.exception;

public class ArtifactNotFoundException extends RuntimeException {

    public ArtifactNotFoundException(Long id) {
        super("Artifact not found: " + id);
    }
}
