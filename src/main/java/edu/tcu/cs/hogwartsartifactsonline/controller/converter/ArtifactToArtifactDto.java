package edu.tcu.cs.hogwartsartifactsonline.controller.converter;

import edu.tcu.cs.hogwartsartifactsonline.controller.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.domain.Artifact;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDto implements Converter<Artifact, ArtifactDto> {

    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto = new ArtifactDto();
        artifactDto.setId(source.getId());
        artifactDto.setName(source.getName());
        artifactDto.setDescription(source.getDescription());
        artifactDto.setImageUrl(source.getImageUrl());
        if (source.getOwner() != null) {
            artifactDto.setOwnerWizardId(source.getOwner().getId());
        }
        return artifactDto;
    }

}
