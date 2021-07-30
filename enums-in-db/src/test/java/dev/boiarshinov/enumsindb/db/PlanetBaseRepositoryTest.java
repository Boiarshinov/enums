package dev.boiarshinov.enumsindb.db;

import dev.boiarshinov.enumsindb.db.PlanetBase.SolarPlanet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PlanetBaseRepositoryTest {

    @Autowired
    PlanetBaseRepository repo;

    @Test
    @Transactional
    void getWithKnownPlanet() {
        PlanetBase planetBase = repo.getById(1L);

        assertEquals(SolarPlanet.MARS, planetBase.getSolarPlanet());
    }

    @Test
    @Transactional
    void getWithUnknownPlanet() {
        PlanetBase planetBase = repo.getById(2L);

        assertEquals(SolarPlanet.UNKNOWN_MIDGET, planetBase.getSolarPlanet());
    }
}