package com.chatpress.v1.artifact;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminArtifactController {

    private final ArtifactService artifactService;
    private final AdminArtifactPageRenderer adminArtifactPageRenderer;

    public AdminArtifactController(
            ArtifactService artifactService,
            AdminArtifactPageRenderer adminArtifactPageRenderer
    ) {
        this.artifactService = artifactService;
        this.adminArtifactPageRenderer = adminArtifactPageRenderer;
    }

    @GetMapping(value = "/admin/artifacts", produces = MediaType.TEXT_HTML_VALUE)
    public String listArtifacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status
    ) {
        Page<Artifact> artifacts = artifactService.listArtifacts(page, size, q, status);
        return adminArtifactPageRenderer.render(artifacts, q, status);
    }
}
