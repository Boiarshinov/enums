package benchmarks.byvalue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Enum100 {
    ENUM_1,
    ENUM_2,
    ENUM_3,
    ENUM_4,
    ENUM_5,
    ENUM_6,
    ENUM_7,
    ENUM_8,
    ENUM_9,
    ENUM_10,
    ENUM_11,
    ENUM_12,
    ENUM_13,
    ENUM_14,
    ENUM_15,
    ENUM_16,
    ENUM_17,
    ENUM_18,
    ENUM_19,
    ENUM_20,
    ENUM_21,
    ENUM_22,
    ENUM_23,
    ENUM_24,
    ENUM_25,
    ENUM_26,
    ENUM_27,
    ENUM_28,
    ENUM_29,
    ENUM_30,
    ENUM_31,
    ENUM_32,
    ENUM_33,
    ENUM_34,
    ENUM_35,
    ENUM_36,
    ENUM_37,
    ENUM_38,
    ENUM_39,
    ENUM_40,
    ENUM_41,
    ENUM_42,
    ENUM_43,
    ENUM_44,
    ENUM_45,
    ENUM_46,
    ENUM_47,
    ENUM_48,
    ENUM_49,
    ENUM_50,
    ENUM_51,
    ENUM_52,
    ENUM_53,
    ENUM_54,
    ENUM_55,
    ENUM_56,
    ENUM_57,
    ENUM_58,
    ENUM_59,
    ENUM_60,
    ENUM_61,
    ENUM_62,
    ENUM_63,
    ENUM_64,
    ENUM_65,
    ENUM_66,
    ENUM_67,
    ENUM_68,
    ENUM_69,
    ENUM_70,
    ENUM_71,
    ENUM_72,
    ENUM_73,
    ENUM_74,
    ENUM_75,
    ENUM_76,
    ENUM_77,
    ENUM_78,
    ENUM_79,
    ENUM_80,
    ENUM_81,
    ENUM_82,
    ENUM_83,
    ENUM_84,
    ENUM_85,
    ENUM_86,
    ENUM_87,
    ENUM_88,
    ENUM_89,
    ENUM_90,
    ENUM_91,
    ENUM_92,
    ENUM_93,
    ENUM_94,
    ENUM_95,
    ENUM_96,
    ENUM_97,
    ENUM_98,
    ENUM_99,
    ENUM_100;

    private static final Map<String, Enum100> MAP = EnumSet.allOf(Enum100.class).stream()
        .collect(Collectors.toMap(Enum100::name, Function.identity()));

    public static Enum100 parseByMap(String name) {
        Enum100 Enum100 = MAP.get(name);
        if (Enum100 == null) throw new IllegalArgumentException();
        return Enum100;
    }

    public static Enum100 parseByStream(String name) {
        return Arrays.stream(Enum100.values())
            .filter(it -> it.name().equals(name))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
