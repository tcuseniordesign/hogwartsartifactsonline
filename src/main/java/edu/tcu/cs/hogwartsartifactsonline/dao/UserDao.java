package edu.tcu.cs.hogwartsartifactsonline.dao;

import edu.tcu.cs.hogwartsartifactsonline.domain.HogwartsUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<HogwartsUser, Integer> {
    HogwartsUser findByUsername(String username);
}
