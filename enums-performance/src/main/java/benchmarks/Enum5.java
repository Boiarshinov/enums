package benchmarks;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Enum5 {
    ENUM_1,
    ENUM_2,
    ENUM_3,
    ENUM_4,
    ENUM_5;

    private static final Map<String, Enum5> MAP = EnumSet.allOf(Enum5.class).stream()
        .collect(Collectors.toMap(Enum5::name, Function.identity()));

    public static Enum5 parseByMap(String name) {
        Enum5 enum5 = MAP.get(name);
        if (enum5 == null) throw new IllegalArgumentException();
        return enum5;
    }

    public static Enum5 parseByStream(String name) {
        return Arrays.stream(Enum5.values())
            .filter(it -> it.name().equals(name))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
