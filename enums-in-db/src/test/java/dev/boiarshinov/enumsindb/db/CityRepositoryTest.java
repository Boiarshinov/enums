package dev.boiarshinov.enumsindb.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class CityRepositoryTest {

    @Autowired
    CityRepository repo;

    @Test
    @Transactional
    void getWithKnownContinent() {
        City city = repo.getById(1L);

        assertEquals(City.Continent.EURASIA, city.getContinent());
    }

    @Test
    @Transactional
    void failWithUnknownContinent() {
        var exception = assertThrows(IllegalArgumentException.class,
            () -> repo.getById(2L).getContinent()
        );

        assertEquals(
            "No enum constant dev.boiarshinov.enumsindb.db.City.Continent.PANGEIA",
            exception.getMessage()
        );
    }
}