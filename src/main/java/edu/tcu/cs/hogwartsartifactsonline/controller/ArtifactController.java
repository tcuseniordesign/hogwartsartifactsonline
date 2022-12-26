package edu.tcu.cs.hogwartsartifactsonline.controller;

import edu.tcu.cs.hogwartsartifactsonline.controller.converter.ArtifactDtoToArtifact;
import edu.tcu.cs.hogwartsartifactsonline.controller.converter.ArtifactToArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.controller.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.domain.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.domain.Result;
import edu.tcu.cs.hogwartsartifactsonline.domain.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.service.ArtifactService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/artifacts")
public class ArtifactController {

    private ArtifactService artifactService;

    private ArtifactDtoToArtifact artifactDtoToArtifact;

    private ArtifactToArtifactDto artifactToArtifactDto;

    public ArtifactController(ArtifactService artifactService, ArtifactDtoToArtifact artifactDtoToArtifact, ArtifactToArtifactDto artifactToArtifactDto) {
        this.artifactService = artifactService;
        this.artifactDtoToArtifact = artifactDtoToArtifact;
        this.artifactToArtifactDto = artifactToArtifactDto;
    }

    @GetMapping
    public Result findAll() {
        List<Artifact> foundArtifacts = artifactService.findAll();
        List<ArtifactDto> list = foundArtifacts.stream()
                .map(artifactToArtifactDto::convert)
                .collect(Collectors.toList());
        Result result = new Result(true, StatusCode.SUCCESS, "Find All Success", list);
        return result;
    }

    @GetMapping("/{artifactId}")
    public Result findById(@PathVariable String artifactId) {
        Artifact foundArtifact = artifactService.findById(artifactId);
        ArtifactDto artifactDto = artifactToArtifactDto.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS, "Find One Success", artifactDto);
    }

    @PostMapping
    public Result save(@Valid @RequestBody ArtifactDto artifactDto) { // We expect a valid @RequestBody
        Artifact newArtifact = artifactDtoToArtifact.convert(artifactDto);
        artifactService.save(newArtifact);
        return new Result(true, StatusCode.SUCCESS, "Save Success");
    }

    /**
     * We are not updating ownership of this artifact.
     * @param artifactId
     * @param artifactDto
     * @return
     */
    @PutMapping("/{artifactId}")
    public Result update(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto) {
        Artifact updatedArtifact = artifactDtoToArtifact.convert(artifactDto);
        artifactService.update(artifactId, updatedArtifact);
        return new Result(true, StatusCode.SUCCESS, "Update Success");
    }

    @DeleteMapping("/{artifactId}")
    public Result delete(@PathVariable String artifactId) {
        artifactService.delete(artifactId);
        return new Result(true, StatusCode.SUCCESS, "Delete Success");
    }

}
