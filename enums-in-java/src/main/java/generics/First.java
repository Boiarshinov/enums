package generics;

public class First extends MyEnum<Second> {

    public static final First FIRST = new First("FIRST", 0);

    private First(String name, int ordinal) {
        super(name, ordinal);
    }
}
