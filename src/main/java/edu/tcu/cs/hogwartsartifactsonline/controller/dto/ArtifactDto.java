package edu.tcu.cs.hogwartsartifactsonline.controller.dto;

import javax.validation.constraints.NotEmpty;

public class ArtifactDto {

    private String id;
    @NotEmpty(message = "name is required")
    private String name;
    @NotEmpty(message = "description is required")
    private String description;
    @NotEmpty(message = "image is required")
    private String imageUrl;

    private Integer ownerWizardId;

    public ArtifactDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getOwnerWizardId() {
        return ownerWizardId;
    }

    public void setOwnerWizardId(Integer ownerWizardId) {
        this.ownerWizardId = ownerWizardId;
    }

}
