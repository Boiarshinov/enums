package commons;

import org.apache.commons.lang3.EnumUtils;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link EnumUtils} from apache commons lang lib.
 */
public class EnumUtilsTest {

    @Test
    void isValidEnum() {
        boolean actualResult = EnumUtils.isValidEnum(BooleanEnum.class, "TRUE");

        assertTrue(actualResult);
    }

    @Test
    void isValidEnumIgnoreCase() {
        boolean actualResult = EnumUtils.isValidEnumIgnoreCase(BooleanEnum.class, "TRUE");

        assertTrue(actualResult);
    }

    @Test
    void getEnum() {
        BooleanEnum parsedEnum = EnumUtils.getEnum(BooleanEnum.class, "TRUE");

        assertEquals(BooleanEnum.TRUE, parsedEnum);
    }

    @Test
    void getEnum_atNonExistentValue() {
        BooleanEnum parsedEnum = EnumUtils.getEnum(BooleanEnum.class, "MAYBE");

        assertNull(parsedEnum);
    }

    @Test
    void getEnumOrElse() {
        BooleanEnum parsedEnum = EnumUtils.getEnum(BooleanEnum.class, "MAYBE", BooleanEnum.FALSE);

        assertEquals(BooleanEnum.FALSE, parsedEnum);
    }

    @Test
    void getEnumList() {
        List<BooleanEnum> enumList = EnumUtils.getEnumList(BooleanEnum.class);

        assertAll(
            () -> assertEquals(2, enumList.size()),
            () -> assertTrue(enumList.contains(BooleanEnum.TRUE)),
            () -> assertTrue(enumList.contains(BooleanEnum.FALSE))
        );
    }

    @Test
    void getEnumMap() {
        Map<String, BooleanEnum> enumMap = EnumUtils.getEnumMap(BooleanEnum.class);

        assertAll(
            () -> assertEquals(2, enumMap.size()),
            () -> assertEquals(BooleanEnum.TRUE, enumMap.get("TRUE")),
            () -> assertEquals(BooleanEnum.FALSE, enumMap.get("FALSE"))
        );
    }

    @Test
    void generateBitVector() {
        long bitVector = EnumUtils.generateBitVector(BooleanEnum.class, BooleanEnum.FALSE);

        assertEquals(0b10, bitVector);
    }

    @Test
    void generateBitVectors() {
        long[] bitVectors = EnumUtils.generateBitVectors(BooleanEnum.class, BooleanEnum.FALSE, BooleanEnum.TRUE);

        assertAll(
            () -> assertEquals(1, bitVectors.length),
            () -> assertEquals(0b11, bitVectors[0])
        );
    }

    @Test
    void processBitVector() {
        EnumSet<BooleanEnum> booleanEnums = EnumUtils.processBitVector(BooleanEnum.class, 0b11);

        assertAll(
            () -> assertEquals(2, booleanEnums.size()),
            () -> assertTrue(booleanEnums.contains(BooleanEnum.TRUE)),
            () -> assertTrue(booleanEnums.contains(BooleanEnum.FALSE))
        );
    }

    private enum BooleanEnum {
        TRUE, FALSE;
    }
}
