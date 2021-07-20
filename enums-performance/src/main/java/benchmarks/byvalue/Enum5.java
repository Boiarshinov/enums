package benchmarks.byvalue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Enum5 {
    ENUM_1("enum-1"),
    ENUM_2("enum-2"),
    ENUM_3("enum-3"),
    ENUM_4("enum-4"),
    UNEXPECTED("unexpected");

    private final String value;

    Enum5(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static final Map<String, Enum5> MAP = EnumSet.allOf(Enum5.class).stream()
        .collect(Collectors.toMap(Enum5::getValue, Function.identity()));

    public static Enum5 parseByMap(String name) {
        return Optional.ofNullable(MAP.get(name))
            .orElse(UNEXPECTED);
    }

    public static Enum5 parseByStream(String name) {
        return Arrays.stream(values())
            .filter(it -> it.getValue().equals(name))
            .findFirst()
            .orElse(UNEXPECTED);
    }

    public static Enum5 parseByForLoop(String name) {
        for (Enum5 enum5: values()) {
            if (enum5.getValue().equals(name)) {
                return enum5;
            }
        }
        return UNEXPECTED;
    }
}
