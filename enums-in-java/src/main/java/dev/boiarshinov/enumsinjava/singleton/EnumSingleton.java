package dev.boiarshinov.enumsinjava.singleton;

import lombok.Getter;

@Getter
public enum EnumSingleton {
    INSTANCE;

    private String mutableField = "Old value";

    public void setMutableField(String mutableField) {
        this.mutableField = mutableField;
    }
}
