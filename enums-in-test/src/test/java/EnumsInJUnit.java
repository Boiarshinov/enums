import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnumsInJUnit {

    @ParameterizedTest
    @EnumSource
    void enumSource(DayOfWeek dayOfWeek) {
        assertNotNull(dayOfWeek);
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"SUNDAY"})
    void enumSourceWithExclusion(DayOfWeek dayOfWeek) {
        assertNotNull(dayOfWeek);
    }

    @ParameterizedTest
    @MethodSource("dayOfWeekProvider")
    void enumSourceWithExclusionViaMethodSource(DayOfWeek dayOfWeek) {
        assertNotNull(dayOfWeek);
    }

    private static Stream<Arguments> dayOfWeekProvider() {
        EnumSet<DayOfWeek> exclusionSet = EnumSet.of(DayOfWeek.SUNDAY);
        return EnumSet.complementOf(exclusionSet).stream()
            .map(Arguments::of);
    }
}
