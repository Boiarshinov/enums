package generics;

import org.junit.jupiter.api.Test;

import static generics.First.FIRST;
import static generics.Second.SECOND;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComparisonTest {

//    @Test
//    void firstCompareFirst() {
//        int comparison = FIRST.compareTo(FIRST);
//
//        assertEquals(0, comparison);
//    }

    @Test
    void firstCompareSecond() {
        int comparison = FIRST.compareTo(SECOND);

        assertEquals(0, comparison);
    }

    @Test
    void secondCompareFirst() {
        int comparison = SECOND.compareTo(FIRST);

        assertEquals(0, comparison);
    }

//    @Test
//    void secondCompareSecond() {
//        int comparison = SECOND.compareTo(SECOND);
//
//        assertEquals(0, comparison);
//    }
}
