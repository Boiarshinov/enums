package dev.boiarshinov.enumsinjava;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Ensemble {
    SOLO(1),
    DUET(2),
    TRIO(3),
    QUARTET(4),
    QUINTET(5),
    SEXTET(6),
    SEPTET(7),
    OCTET(8);

    private final int musiciansCount;

    private static final Map<Integer, Ensemble> COUNT_BY_ENSEMBLE = EnumSet.allOf(Ensemble.class).stream()
        .collect(Collectors.toMap(Ensemble::getMusiciansCount, Function.identity()));

    //good way
    public static Ensemble byCount(int count) {
        return Optional.ofNullable(COUNT_BY_ENSEMBLE.get(count))
            .orElseThrow(() -> new IllegalArgumentException(String.format("Have no ensemble for count '%s'", count)));
    }

    //bad way
    public static Ensemble fromCount(int count) {
        return Arrays.stream(Ensemble.values())
            .filter(it -> it.getMusiciansCount() == count)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Have no ensemble for count '%s'", count)));
    }
}
