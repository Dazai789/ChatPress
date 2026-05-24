package com.chatpress.v1.artifact;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicPageController {

    private final ArtifactService artifactService;
    private final PublicPageRenderer publicPageRenderer;

    public PublicPageController(ArtifactService artifactService, PublicPageRenderer publicPageRenderer) {
        this.artifactService = artifactService;
        this.publicPageRenderer = publicPageRenderer;
    }

    @GetMapping(value = "/p/{slug}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getPublicPage(@PathVariable String slug) {
        return artifactService.getArtifactBySlug(slug)
                .map(artifact -> ResponseEntity.ok(publicPageRenderer.render(artifact)))
                .orElse(ResponseEntity.notFound().build());
    }
}
