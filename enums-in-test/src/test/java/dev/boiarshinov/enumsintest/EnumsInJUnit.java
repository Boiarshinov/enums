package dev.boiarshinov.enumsintest;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.EnumSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnumsInJUnit {

    @ParameterizedTest
    @EnumSource
    void enumSource(Continent continent) {
        assertNotNull(continent);
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"UNKNOWN_MIDGET"})
    void enumSourceWithExclusion(SolarPlanet solarPlanet) {
        assertNotNull(solarPlanet);
    }

    @ParameterizedTest
    @MethodSource("solarPlanetProvider")
    void enumSourceWithExclusionViaMethodSource(SolarPlanet solarPlanet) {
        assertNotNull(solarPlanet);
    }

    private static Stream<Arguments> solarPlanetProvider() {
        EnumSet<SolarPlanet> exclusionSet = EnumSet.of(SolarPlanet.UNKNOWN_MIDGET);
        return EnumSet.complementOf(exclusionSet).stream()
            .map(Arguments::of);
    }
}
