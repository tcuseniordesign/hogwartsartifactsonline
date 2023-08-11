package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();


    public Wizard() {
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addArtifact(Artifact artifact) {
        // Set artifact owner.
        artifact.setOwner(this);

        // Add this artifact to this wizard.
        this.artifacts.add(artifact);
    }

    public void removeArtifact(Artifact artifact) {
        // Remove artifact owner.
        artifact.setOwner(null);

        // Remove this artifact from artifacts.
        this.artifacts.remove(artifact);
    }

    public void removeAllArtifacts() {
        this.artifacts.stream().forEach(artifact -> artifact.setOwner(null));

        // I think it is better to set it to an empty list instead of null.
        this.artifacts = new ArrayList<>();
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size(); // artifacts has been initialized.
    }

}
