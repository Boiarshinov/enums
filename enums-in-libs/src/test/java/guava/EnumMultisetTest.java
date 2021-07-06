package guava;

import com.google.common.collect.EnumMultiset;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumMultisetTest {

    @Test
    void createAndGetSize() {
        EnumMultiset<Planet> planets = EnumMultiset.create(Planet.class);
        planets.add(Planet.EARTH);
        planets.add(Planet.MARS);
        planets.add(Planet.VENUS, 2);

        assertEquals(4, planets.size());
    }

    @Test
    void count() {
        EnumMultiset<Planet> planets = EnumMultiset.create(Planet.class);
        planets.add(Planet.URANUS, 2);
        planets.add(Planet.URANUS);

        assertEquals(3, planets.count(Planet.URANUS));
    }

    private enum Planet {
        MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNIUM, PLUTO
    }
}
