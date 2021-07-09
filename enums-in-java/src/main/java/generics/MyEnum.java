package generics;

public abstract class MyEnum<E extends MyEnum> {

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
}
