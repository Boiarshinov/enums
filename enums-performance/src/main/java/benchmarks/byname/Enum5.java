package benchmarks.byname;

import com.google.common.base.Enums;
import org.apache.commons.lang3.EnumUtils;

public enum Enum5 {
    ENUM_1,
    ENUM_2,
    ENUM_3,
    ENUM_4,
    UNEXPECTED;

    public static Enum5 diyValueOf(String name) {
        try {
            return Enum5.valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return UNEXPECTED;
        }
    }

    public static Enum5 diyStaticValueOf(String name) {
        try {
            return Enum.valueOf(Enum5.class, name);
        } catch (IllegalArgumentException ignored) {
            return UNEXPECTED;
        }
    }

    public static Enum5 apacheValueOf(String name) {
        return EnumUtils.getEnum(Enum5.class, name, UNEXPECTED);
    }

    public static Enum5 guavaValueOf(String name) {
        return Enums.getIfPresent(Enum5.class, name).or(UNEXPECTED);
    }
}
