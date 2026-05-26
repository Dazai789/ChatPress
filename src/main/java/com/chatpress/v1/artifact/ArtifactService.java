package com.chatpress.v1.artifact;

import com.chatpress.v1.artifact.exception.ArtifactNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class ArtifactService {

    private final ArtifactRepository artifactRepository;
    private final MarkdownRenderer markdownRenderer;

    public ArtifactService(ArtifactRepository artifactRepository, MarkdownRenderer markdownRenderer) {
        this.artifactRepository = artifactRepository;
        this.markdownRenderer = markdownRenderer;
    }

    public Artifact createArtifact(String title, String sourceContent) {
        String finalSlug = generateSlug(title);

        Artifact artifact = new Artifact(title, finalSlug, sourceContent, markdownRenderer.render(sourceContent));
        artifact.setStatus(Artifact.Status.PUBLISHED);
        return artifactRepository.save(artifact);
    }

    public List<Artifact> listArtifacts() {
        return artifactRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Artifact getArtifactOrThrow(Long id) {
        return artifactRepository.findById(id)
                .orElseThrow(() -> new ArtifactNotFoundException(id));
    }

    public Optional<Artifact> getArtifactBySlug(String slug) {
        return artifactRepository.findBySlug(slug);
    }

    public Optional<Artifact> getPublishedArtifactBySlug(String slug) {
        return artifactRepository.findBySlugAndStatus(slug, Artifact.Status.PUBLISHED);
    }

    public Artifact updateArtifactOrThrow(Long id, String title, String sourceContent) {
        Artifact artifact = getArtifactOrThrow(id);
        artifact.setTitle(title);
        artifact.setSourceContent(sourceContent);
        artifact.setRenderedHtml(markdownRenderer.render(sourceContent));
        return artifactRepository.save(artifact);
    }

    public Artifact updateArtifactStatusOrThrow(Long id, Artifact.Status status) {
        Artifact artifact = getArtifactOrThrow(id);
        artifact.setStatus(status);
        return artifactRepository.save(artifact);
    }

    public void deleteArtifactOrThrow(Long id) {
        getArtifactOrThrow(id);
        artifactRepository.deleteById(id);
    }

    private String generateSlug(String title) {
        String baseSlug = slugifyTitle(title);
        String candidateSlug = baseSlug;
        int suffix = 2;
        while (artifactRepository.findBySlug(candidateSlug).isPresent()) {
            candidateSlug = baseSlug + "-" + suffix;
            suffix++;
        }
        return candidateSlug;
    }

    private String slugifyTitle(String title) {
        String slug = title.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-+|-+$)", "")
                .replaceAll("-+", "-");

        if (slug.isBlank()) {
            return "artifact";
        }
        return slug;
    }
}
