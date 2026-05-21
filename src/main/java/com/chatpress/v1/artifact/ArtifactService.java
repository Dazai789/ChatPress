package com.chatpress.v1.artifact;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    public ArtifactService(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    public Artifact createArtifact(String title, String slug, String sourceContent) {
        Artifact artifact = new Artifact(title, slug, sourceContent, sourceContent);
        artifact.setStatus("published");
        return artifactRepository.save(artifact);
    }

    public List<Artifact> listArtifacts() {
        return artifactRepository.findAll();
    }

    public Optional<Artifact> getArtifact(Long id) {
        return artifactRepository.findById(id);
    }

    public Optional<Artifact> updateArtifact(Long id, String title, String slug, String sourceContent) {
        return artifactRepository.findById(id)
                .map(artifact -> {
                    artifact.setTitle(title);
                    artifact.setSlug(slug);
                    artifact.setSourceContent(sourceContent);
                    artifact.setRenderedHtml(sourceContent);
                    return artifactRepository.save(artifact);
                });
    }

    public boolean deleteArtifact(Long id) {
        if (!artifactRepository.existsById(id)) {
            return false;
        }

        artifactRepository.deleteById(id);
        return true;
    }
}
