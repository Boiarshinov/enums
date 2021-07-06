package guava;

import com.google.common.collect.BiMap;
import com.google.common.collect.EnumBiMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnumBiMapTest {

    private EnumBiMap<Numbers, Ensemble> biMap;

    @BeforeEach
    void setUp() {
        biMap = EnumBiMap.create(Numbers.class, Ensemble.class);
        biMap.put(Numbers.ONE, Ensemble.SOLO);
        biMap.put(Numbers.TWO, Ensemble.DUET);
        biMap.put(Numbers.THREE, Ensemble.TRIO);
        biMap.put(Numbers.FOUR, Ensemble.QUARTET);
    }

    @Test
    void inverse() {
        BiMap<Ensemble, Numbers> inverted = biMap.inverse();

        assertEquals(Numbers.ONE, inverted.get(Ensemble.SOLO));
    }

    @Test
    void putExistedValue() {
        assertThrows(IllegalArgumentException.class,
                () -> biMap.put(Numbers.TWO, Ensemble.TRIO));
    }

    @Test
    void forcePut() {
        biMap.forcePut(Numbers.TWO, Ensemble.TRIO);

        assertEquals(3, biMap.size());
        assertFalse(biMap.containsKey(Numbers.THREE));
    }

    enum Numbers {
        ONE, TWO, THREE, FOUR
    }

    @Getter
    @RequiredArgsConstructor
    enum Ensemble {
        SOLO(1),
        DUET(2),
        TRIO(3),
        QUARTET(4);

        private final int musiciansCount;
    }

}
