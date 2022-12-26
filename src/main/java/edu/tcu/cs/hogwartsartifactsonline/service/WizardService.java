package edu.tcu.cs.hogwartsartifactsonline.service;

import edu.tcu.cs.hogwartsartifactsonline.dao.ArtifactDao;
import edu.tcu.cs.hogwartsartifactsonline.dao.WizardDao;
import edu.tcu.cs.hogwartsartifactsonline.domain.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.domain.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WizardService {

    private WizardDao wizardDao;
    private ArtifactDao artifactDao;

    public WizardService(WizardDao wizardDao, ArtifactDao artifactDao) {
        this.wizardDao = wizardDao;
        this.artifactDao = artifactDao;
    }

    public List<Wizard> findAll() {
        List<Wizard> list = new ArrayList<>();
        List<Wizard> wizards = wizardDao.findAll();
        wizards.forEach(list::add);
        return list;
    }

    public Wizard findById(Integer wizardId) {
        return wizardDao.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public Wizard save(Wizard newWizard) {
        return wizardDao.save(newWizard);
    }

    public Wizard update(Integer wizardId, Wizard updatedWizard) {
        wizardDao.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
        updatedWizard.setId(wizardId);
        return wizardDao.save(updatedWizard);
    }

    public void deleteById(Integer wizardId) {
        wizardDao.findById(wizardId).orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
        wizardDao.deleteById(wizardId);
    }

    public void assignArtifact(Integer wizardId, String artifactId) {
        // find this artifact by id from DB
        Artifact artifactToBeAssigned = artifactDao.findById(artifactId).get();
        Wizard wizard = wizardDao.findById(wizardId).get();

        if (artifactToBeAssigned.getOwner() != null) {
            artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
        }
        wizard.addArtifact(artifactToBeAssigned);
    }

}
