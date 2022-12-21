package edu.tcu.cs.hogwartsartifactsonline.unit.service;

import edu.tcu.cs.hogwartsartifactsonline.dao.ArtifactDao;
import edu.tcu.cs.hogwartsartifactsonline.domain.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.service.ArtifactService;
import edu.tcu.cs.hogwartsartifactsonline.exception.ObjectNotFoundException;
import edu.tcu.cs.hogwartsartifactsonline.utils.IdWorker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// For JUnit 5, need to use @ExtendWith
@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactDao artifactDao;

    @Mock
    IdWorker idWorker;
    @InjectMocks
    ArtifactService service;

    List<Artifact> listOfArtifacts;

    @BeforeEach
    void setUp() {
        // arrange
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absorb (as well as return) the light from any light source to provide cover to the user.");
        a1.setImageUrl("imageUrl");

        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("imageURL");

        listOfArtifacts = new ArrayList<>();
        listOfArtifacts.add(a1);
        listOfArtifacts.add(a2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAll() {
        // arrange, defines the behavior of Mock object artifactDao
        when(artifactDao.findAll()).thenReturn(listOfArtifacts);

        // act
        List<Artifact> actualListOfArtifacts = service.findAll();

        // assert
        assertEquals(listOfArtifacts, actualListOfArtifacts);
        verify(artifactDao, times(1)).findAll();
    }

    @Test
    void testFindByIdIfFound() {
        // arrange, defines the behavior of Mock object artifactDao
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("imageURL");

        when(artifactDao.findById(Mockito.any(String.class))).thenReturn(Optional.of(a));

        // act
        Artifact returnedArtifact = service.findById("1250808601744904192");

        // assert
        assertEquals(a, returnedArtifact);
        verify(artifactDao, times(1)).findById(Mockito.any(String.class));
    }

    @Test
    void testFindByIdIfNotFound() {
        // arrange, defines the behavior of Mock object artifactDao
        when(artifactDao.findById(Mockito.any(String.class))).thenReturn(Optional.ofNullable(null));

        // assert
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> {
            // act
            Artifact returnedArtifact = service.findById("1250808601744904192");
        });
        verify(artifactDao, times(1)).findById(Mockito.any(String.class));
        assertEquals("Could not find artifact with id 1250808601744904192 :(", exception.getMessage());
    }

    @Test
    void testSave() {
        // arrange, defines the behavior of Mock object artifactDao
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 3");
        newArtifact.setDescription("Description...");
        newArtifact.setImageUrl("imageURL");
        when(artifactDao.save(Mockito.any(Artifact.class))).thenReturn(newArtifact);

        // act
        Artifact saved = service.save(newArtifact);

        // assert
        assertEquals(saved.getId(), newArtifact.getId());
        assertEquals(saved.getName(), newArtifact.getName());
        assertEquals(saved.getDescription(), newArtifact.getDescription());
        assertEquals(saved.getImageUrl(), newArtifact.getImageUrl());
        verify(artifactDao, times(1)).save(newArtifact);
    }

    @Test
    void testUpdateIfFound() {
        // arrange, defines the behavior of Mock object artifactDao
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("imageURL");

        Artifact update = new Artifact();
        update.setName("Invisibility Cloak");
        update.setDescription("A new description.");
        update.setImageUrl("imageURL");

        Artifact updated = new Artifact();
        updated.setId("1250808601744904192");
        updated.setName("Invisibility Cloak");
        updated.setDescription("A new description.");
        updated.setImageUrl("imageURL");

        when(artifactDao.findById(Mockito.any(String.class))).thenReturn(Optional.of(artifact));

        when(artifactDao.save(Mockito.any(Artifact.class))).thenReturn(updated);

        // act
        Artifact updatedArtifact = service.update("1250808601744904192", updated);

        // assert
        assertEquals("1250808601744904192", updatedArtifact.getId());
        verify(artifactDao, times(1)).save(Mockito.any(Artifact.class));
        verify(artifactDao, times(1)).findById("1250808601744904192");
    }

    @Test
    void testUpdateIfNotFound() {
        // arrange, defines the behavior of Mock object artifactDao
        Artifact updated = new Artifact();
        updated.setName("Invisibility Cloak");
        updated.setDescription("A new description.");
        updated.setImageUrl("imageURL");

        when(artifactDao.findById(Mockito.any(String.class))).thenReturn(Optional.ofNullable(null));

        // act
        assertThrows(ObjectNotFoundException.class, () -> {
            service.update("123456", updated);
        });

        // assert
        verify(artifactDao, times(1)).findById("123456");
    }

    @Test
    void testDeleteIfFound() {
        // arrange, defines the behavior of Mock object artifactDao
        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("imageURL");

        when(artifactDao.findById(Mockito.any(String.class))).thenReturn(Optional.of(artifact));
        doNothing().when(artifactDao).deleteById(Mockito.any(String.class));

        // act
        service.delete("1250808601744904192");

        // assert
        //verify artifactDao is called 1 time, and the deleteById method is called with an id
        verify(artifactDao, times(1)).deleteById("1250808601744904192");
    }

    @Test
    void testDeleteIfNotFound() {
        // arrange, defines the behavior of Mock object artifactDao
        when(artifactDao.findById(Mockito.any(String.class))).thenReturn(Optional.ofNullable(null));

        // act
        assertThrows(ObjectNotFoundException.class, () -> {
            service.delete("123456");
        });

        // assert
        verify(artifactDao, times(1)).findById("123456");
    }
}
