package dev.boiarshinov.enumsinjava.initorder;

public enum Pine {
    FIR, CEDAR;

    static {
        System.out.println("Static block");
    }

    {
        System.out.println("Code block");
    }

    Pine() {
        System.out.println("Constructor");
    }
}
