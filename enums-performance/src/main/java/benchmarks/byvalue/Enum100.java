package benchmarks.byvalue;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Enum100 {
    ENUM_1("enum-1"),
    ENUM_2("enum-2"),
    ENUM_3("enum-3"),
    ENUM_4("enum-4"),
    ENUM_5("enum-5"),
    ENUM_6("enum-6"),
    ENUM_7("enum-7"),
    ENUM_8("enum-8"),
    ENUM_9("enum-9"),
    ENUM_10("enum-10"),
    ENUM_11("enum-11"),
    ENUM_12("enum-12"),
    ENUM_13("enum-13"),
    ENUM_14("enum-14"),
    ENUM_15("enum-15"),
    ENUM_16("enum-16"),
    ENUM_17("enum-17"),
    ENUM_18("enum-18"),
    ENUM_19("enum-19"),
    ENUM_20("enum-20"),
    ENUM_21("enum-21"),
    ENUM_22("enum-22"),
    ENUM_23("enum-23"),
    ENUM_24("enum-24"),
    ENUM_25("enum-25"),
    ENUM_26("enum-26"),
    ENUM_27("enum-27"),
    ENUM_28("enum-28"),
    ENUM_29("enum-29"),
    ENUM_30("enum-30"),
    ENUM_31("enum-31"),
    ENUM_32("enum-32"),
    ENUM_33("enum-33"),
    ENUM_34("enum-34"),
    ENUM_35("enum-35"),
    ENUM_36("enum-36"),
    ENUM_37("enum-37"),
    ENUM_38("enum-38"),
    ENUM_39("enum-39"),
    ENUM_40("enum-40"),
    ENUM_41("enum-41"),
    ENUM_42("enum-42"),
    ENUM_43("enum-43"),
    ENUM_44("enum-44"),
    ENUM_45("enum-45"),
    ENUM_46("enum-46"),
    ENUM_47("enum-47"),
    ENUM_48("enum-48"),
    ENUM_49("enum-49"),
    ENUM_50("enum-50"),
    ENUM_51("enum-51"),
    ENUM_52("enum-52"),
    ENUM_53("enum-53"),
    ENUM_54("enum-54"),
    ENUM_55("enum-55"),
    ENUM_56("enum-56"),
    ENUM_57("enum-57"),
    ENUM_58("enum-58"),
    ENUM_59("enum-59"),
    ENUM_60("enum-60"),
    ENUM_61("enum-61"),
    ENUM_62("enum-62"),
    ENUM_63("enum-63"),
    ENUM_64("enum-64"),
    ENUM_65("enum-65"),
    ENUM_66("enum-66"),
    ENUM_67("enum-67"),
    ENUM_68("enum-68"),
    ENUM_69("enum-69"),
    ENUM_70("enum-70"),
    ENUM_71("enum-71"),
    ENUM_72("enum-72"),
    ENUM_73("enum-73"),
    ENUM_74("enum-74"),
    ENUM_75("enum-75"),
    ENUM_76("enum-76"),
    ENUM_77("enum-77"),
    ENUM_78("enum-78"),
    ENUM_79("enum-79"),
    ENUM_80("enum-80"),
    ENUM_81("enum-81"),
    ENUM_82("enum-82"),
    ENUM_83("enum-83"),
    ENUM_84("enum-84"),
    ENUM_85("enum-85"),
    ENUM_86("enum-86"),
    ENUM_87("enum-87"),
    ENUM_88("enum-88"),
    ENUM_89("enum-89"),
    ENUM_90("enum-90"),
    ENUM_91("enum-91"),
    ENUM_92("enum-92"),
    ENUM_93("enum-93"),
    ENUM_94("enum-94"),
    ENUM_95("enum-95"),
    ENUM_96("enum-96"),
    ENUM_97("enum-97"),
    ENUM_98("enum-98"),
    ENUM_99("enum-99"),
    UNEXPECTED("unexpected");

    private final String value;

    Enum100(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private static final Map<String, Enum100> MAP = EnumSet.allOf(Enum100.class).stream()
        .collect(Collectors.toMap(Enum100::getValue, Function.identity()));

    public static Enum100 parseByMap(String name) {
        return Optional.ofNullable(MAP.get(name))
            .orElse(UNEXPECTED);
    }

    public static Enum100 parseByStream(String name) {
        return Arrays.stream(values())
            .filter(it -> it.getValue().equals(name))
            .findFirst()
            .orElse(UNEXPECTED);
    }

    public static Enum100 parseByForLoop(String name) {
        for (Enum100 Enum100: values()) {
            if (Enum100.getValue().equals(name)) {
                return Enum100;
            }
        }
        return UNEXPECTED;
    }
}
