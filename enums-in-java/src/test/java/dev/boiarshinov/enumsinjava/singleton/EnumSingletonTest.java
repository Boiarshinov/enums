package dev.boiarshinov.enumsinjava.singleton;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumSingletonTest {

    @Test
    void mutateField() {
        EnumSingleton enumSingleton = EnumSingleton.INSTANCE;
        assertEquals("Old value", enumSingleton.getMutableField());

        enumSingleton.setMutableField("New value");

        assertEquals("New value", enumSingleton.getMutableField());
    }
}
