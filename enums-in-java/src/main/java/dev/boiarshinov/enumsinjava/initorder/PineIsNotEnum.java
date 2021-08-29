package dev.boiarshinov.enumsinjava.initorder;

// No one can extend Enum! It's not permitted by compiler
public class PineIsNotEnum /*extends Enum<PineIsNotEnum>*/ {

    public static final PineIsNotEnum FIR;
    public static final PineIsNotEnum CEDAR;
    private static final PineIsNotEnum[] VALUES;

    protected PineIsNotEnum(String name, int ordinal) {
        //super(name, ordinal);
        System.out.println("Code block");
        System.out.println("Constructor");
    }

    static {
        FIR = new PineIsNotEnum("FIR", 0);
        CEDAR = new PineIsNotEnum("CEDAR", 1);
        VALUES = new PineIsNotEnum[] {FIR, CEDAR};
        System.out.println("Static block");
    }

    public static PineIsNotEnum[] values() {
        return VALUES.clone();
    }

    //not compiled because PineIsNotEnum not extend Enum class
//    public static PineIsNotEnum valueOf(String name) {
//        return Enum.valueOf(PineIsNotEnum.class, name);
//    }
}
