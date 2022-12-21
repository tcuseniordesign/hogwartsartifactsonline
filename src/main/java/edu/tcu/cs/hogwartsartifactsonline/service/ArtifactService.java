package edu.tcu.cs.hogwartsartifactsonline.service;

import edu.tcu.cs.hogwartsartifactsonline.dao.ArtifactDao;
import edu.tcu.cs.hogwartsartifactsonline.domain.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.utils.IdWorker;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ArtifactService {

    private ArtifactDao artifactDao;
    private IdWorker idWorker;

    // Spring will automatically inject an instance of ArtifactDao and an instance of IdWorker into this class
    public ArtifactService(ArtifactDao artifactDao, IdWorker idWorker) {
        this.artifactDao = artifactDao;
        this.idWorker = idWorker;
    }

    public List<Artifact> findAll() {
        return artifactDao.findAll();
    }

    public Artifact findById(String artifactId) {
        return artifactDao.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
    }

    public Artifact save(Artifact newArtifact) {
        newArtifact.setId(idWorker.nextId() + "");
        return artifactDao.save(newArtifact);
    }

    /**
     * We are not updating ownership of an artifact using this method
     * @param artifactId
     * @param updatedArtifact
     * @return
     */
    public Artifact update(String artifactId, Artifact updatedArtifact) {
        // A fancier way to implement update, same as the above commented statements
        return artifactDao.findById(artifactId)
                .map(oldArtifact -> {
                    oldArtifact.setName(updatedArtifact.getName());
                    oldArtifact.setDescription(updatedArtifact.getDescription());
                    oldArtifact.setImageUrl(updatedArtifact.getImageUrl());
                    return artifactDao.save(oldArtifact);
                })
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
    }

    public void delete(String artifactId) {
        artifactDao.findById(artifactId)
                .orElseThrow(() -> new ObjectNotFoundException("artifact", artifactId));
        artifactDao.deleteById(artifactId);
    }
}
