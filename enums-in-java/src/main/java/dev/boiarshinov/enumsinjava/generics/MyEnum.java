package dev.boiarshinov.enumsinjava.generics;

public abstract class MyEnum<E extends MyEnum>
    implements Comparable<E> {

    private final String name;
    private final int ordinal;

    protected MyEnum(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public String name() {
        return name;
    }

    public int ordinal() {
        return ordinal;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public final boolean equals(Object other) {
        return this == other;
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(E o) {
        return this.ordinal - o.ordinal();
    }

    public final Class<E> getDeclaringClass() {
        Class<? extends MyEnum> clazz = getClass();
        Class<?> zuper = clazz.getSuperclass();
        return (zuper == Enum.class)
                ? (Class<E>) clazz
                : (Class<E>) zuper;
    }

}
