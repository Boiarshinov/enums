package generics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardSuitTest {

    @Test
    void valueOf() {
        CardSuit heart = CardSuit.valueOf("HEART");

        assertEquals(CardSuit.HEART, heart);
    }
}
