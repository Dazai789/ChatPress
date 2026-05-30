package com.chatpress.artifact;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class ArtifactServiceTransactionTest {

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void createArtifactIsCommitted() {
        Artifact artifact = artifactService.createArtifact("Tx Test", "# Tx Test", null, "tester");

        Artifact found = artifactRepository.findById(artifact.getId()).orElseThrow();
        assertThat(found.getTitle()).isEqualTo("Tx Test");
    }

    @Test
    void manualRollbackDoesNotPersist() {
        TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionDefinition());

        Artifact artifact = new Artifact(
                "Rollback Test", "rollback-test", "# Rollback", "<p>Rollback</p>", "tester"
        );
        artifactRepository.save(artifact);

        transactionManager.rollback(tx);

        assertThat(artifactRepository.findBySlug("rollback-test")).isEmpty();
    }

    @Test
    void slugUniqueConstraintPreventsDuplicate() {
        Artifact first = new Artifact("Dup Slug", "dup-slug", "# Dup", "<p>Dup</p>", "tester");
        artifactRepository.save(first);

        Artifact second = new Artifact("Dup Slug 2", "dup-slug", "# Dup 2", "<p>Dup 2</p>", "tester");
        assertThatThrownBy(() -> artifactRepository.save(second))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void deleteArtifactIsCommitted() {
        Artifact artifact = artifactService.createArtifact("Delete Tx", "# Delete Tx", null, "tester");
        Long id = artifact.getId();

        artifactService.deleteArtifactOrThrow(id, "tester");

        assertThat(artifactRepository.findById(id)).isEmpty();
    }

    @Test
    void v4MigrationIndexesApplied() {
        artifactService.createArtifact("Index Test", "# Index Test", null, "tester");

        var results = artifactService.listArtifacts(0, 10, "Index", "published", null, "tester");
        assertThat(results.getTotalElements()).isEqualTo(1);
    }
}
