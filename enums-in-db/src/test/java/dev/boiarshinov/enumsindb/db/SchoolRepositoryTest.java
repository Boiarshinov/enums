package dev.boiarshinov.enumsindb.db;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class SchoolRepositoryTest {

    @Autowired
    SchoolRepository repo;

    @Test
    @Transactional
    void saveAndGet() {
        School school = new School();
        school.setNumber(1811);
        school.setType(School.Type.MEDIUM);

        repo.save(school);

        School fromDb = repo.getById(school.getId());
        assertEquals(school.getNumber(), fromDb.getNumber());
        assertEquals(school.getType(), fromDb.getType());
    }
}
