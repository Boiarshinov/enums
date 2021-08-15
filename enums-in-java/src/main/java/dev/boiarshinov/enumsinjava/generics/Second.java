package dev.boiarshinov.enumsinjava.generics;

public class Second extends MyEnum<First> {

    public static final Second SECOND = new Second("SECOND", 0);

    private Second(String name, int ordinal) {
        super(name, ordinal);
    }
}
