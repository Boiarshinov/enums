import org.junit.jupiter.api.Test;

/**
 * Performance test.
 * It's better to use JMH here but it's uncommon for me
 */
public class EnsembleTest {

    private static final int TIMES = 1_000_000;

    @Test
    void fromCountByMap() {
        final long before = System.currentTimeMillis();

        for (int i = 0; i < TIMES; i++) {
            final Ensemble ensemble = Ensemble.byCount(5);
        }

        final long duration = System.currentTimeMillis() - before;
        System.out.println("Duration: " + duration);
    }

    @Test
    void fromCountByStream() {
        final long before = System.currentTimeMillis();

        for (int i = 0; i < TIMES; i++) {
            final Ensemble ensemble = Ensemble.fromCount(5);
        }

        final long duration = System.currentTimeMillis() - before;
        System.out.println("Duration: " + duration);
    }
}
