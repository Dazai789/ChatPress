package com.chatpress.v1.artifact.exception;

public class DuplicateSlugException extends RuntimeException {

    public DuplicateSlugException(String slug) {
        super("Artifact slug already exists: " + slug);
    }
}
