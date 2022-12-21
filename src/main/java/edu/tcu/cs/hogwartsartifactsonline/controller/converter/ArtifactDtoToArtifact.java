package edu.tcu.cs.hogwartsartifactsonline.controller.converter;

import edu.tcu.cs.hogwartsartifactsonline.controller.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.domain.Artifact;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifact implements Converter<ArtifactDto, Artifact> {
    @Override
    public Artifact convert(ArtifactDto source) {
        Artifact artifact = new Artifact();
        artifact.setName(source.getName());
        artifact.setDescription(source.getDescription());
        artifact.setImageUrl(source.getImageUrl());
        return artifact;
    }
}
