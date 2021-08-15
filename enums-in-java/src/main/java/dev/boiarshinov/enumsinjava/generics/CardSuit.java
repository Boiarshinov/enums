package dev.boiarshinov.enumsinjava.generics;

public class CardSuit extends MyEnum<CardSuit> {

    public static final CardSuit ACE = new CardSuit("ACE", 0);
    public static final CardSuit CLUB = new CardSuit("CLUB", 1);
    public static final CardSuit HEART = new CardSuit("HEART", 2);
    public static final CardSuit DIAMOND = new CardSuit("DIAMOND", 3);

    private CardSuit(String name, int ordinal) {
        super(name, ordinal);
    }

    public static CardSuit valueOf(String name) {
        if (name.equals("ACE")) return ACE;
        if (name.equals("CLUB")) return CLUB;
        if (name.equals("HEART")) return HEART;
        if (name.equals("DIAMOND")) return DIAMOND;
        throw new IllegalArgumentException("Illegal name: " + name);
    }
}
