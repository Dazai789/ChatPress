package com.chatpress.v1.artifact;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/artifacts")
public class ArtifactController {

    private final ArtifactService artifactService;

    public ArtifactController(ArtifactService artifactService) {
        this.artifactService = artifactService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Artifact createArtifact(@Valid @RequestBody ArtifactRequest request) {
        return artifactService.createArtifact(
                request.title(),
                request.slug(),
                request.sourceContent()
        );
    }

    @GetMapping
    public List<Artifact> listArtifacts() {
        return artifactService.listArtifacts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artifact> getArtifact(@PathVariable Long id) {
        return artifactService.getArtifact(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artifact> updateArtifact(
            @PathVariable Long id,
            @Valid @RequestBody ArtifactRequest request
    ) {
        return artifactService.updateArtifact(
                        id,
                        request.title(),
                        request.slug(),
                        request.sourceContent()
                )
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtifact(@PathVariable Long id) {
        if (!artifactService.deleteArtifact(id)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    public record ArtifactRequest(
            @NotBlank
            @Size(max = 200)
            String title,

            @NotBlank
            @Size(max = 200)
            String slug,

            @NotBlank
            String sourceContent
    ) {
    }
}
