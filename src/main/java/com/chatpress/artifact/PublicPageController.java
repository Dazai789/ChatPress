package com.chatpress.artifact;

import com.chatpress.artifact.renderer.PublicPageRenderer;

import com.chatpress.artifact.ArtifactService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicPageController {

    private final ArtifactService artifactService;
    private final PublicPageRenderer publicPageRenderer;
    private final PublicPageCache cache;

    public PublicPageController(ArtifactService artifactService,
                                PublicPageRenderer publicPageRenderer,
                                PublicPageCache cache) {
        this.artifactService = artifactService;
        this.publicPageRenderer = publicPageRenderer;
        this.cache = cache;
    }

    @GetMapping(value = "/p/{slug}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getPublicPage(@PathVariable String slug) {
        String cached = cache.get(slug);
        if (cached != null) {
            return ResponseEntity.ok(cached);
        }

        return artifactService.getPublishedArtifactBySlug(slug)
                .map(artifact -> {
                    String html = publicPageRenderer.render(artifact);
                    cache.put(slug, html);
                    return ResponseEntity.ok(html);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
