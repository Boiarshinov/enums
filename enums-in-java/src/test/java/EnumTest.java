import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EnumTest {

    @Test
    @Disabled("Ошибка в спецификации Java. Написано, что бросается " +
        "только IllegalArgumentException, но в приведенном кейсе бросается NPE")
    void valueOfAtNullValueByJavadoc() {
        assertThrows(IllegalArgumentException.class,
            () -> DayOfWeek.valueOf(null));
        Enum.valueOf(DayOfWeek.class, "MONDAY");
    }

    @Test
    void valueOfAtNullValueExpectNPE() {
        assertThrows(NullPointerException.class,
            () -> DayOfWeek.valueOf(null));
    }
}
