package dev.boiarshinov.enumsinjava.initorder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageConst {
    public static final String ENUM_STATIC_BLOCK = "Enum static block";
    public static final String ENUM_CODE_BLOCK = "Enum code block";
    public static final String ENUM_CONSTRUCTOR = "Enum constructor";
    public static final String TEST_STATIC_BLOCK = "Test static block";
    public static final String TEST_METHOD_START = "Test method start";
    public static final String TEST_METHOD_FINISH = "Test method finish";
    public static final String ENUM_METHOD = "Enum method";
}
