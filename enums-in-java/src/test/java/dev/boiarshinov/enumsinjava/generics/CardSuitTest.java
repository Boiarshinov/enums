package dev.boiarshinov.enumsinjava.generics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardSuitTest {

    @Test
    void valueOf() {
        CardSuit heart = CardSuit.valueOf("HEART");

        assertEquals(CardSuit.HEART, heart);
    }

    @Test
    void compareTo() {
        int comparisonResult = CardSuit.DIAMOND.compareTo(CardSuit.ACE);
        assertTrue(comparisonResult > 0);
    }
}
