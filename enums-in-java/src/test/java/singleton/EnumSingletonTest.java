package singleton;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumSingletonTest {

    static {
        MessageHolder.messages.add("Test static block");
    }

    @Test
    void initOrder() {
        List<String> expectedMessageSequence = List.of(
                MessageConst.TEST_STATIC_BLOCK,
                MessageConst.TEST_METHOD_START,
                MessageConst.ENUM_CODE_BLOCK,
                MessageConst.ENUM_CONSTRUCTOR,
                MessageConst.ENUM_STATIC_BLOCK,
                MessageConst.ENUM_METHOD,
                MessageConst.TEST_METHOD_FINISH
        );

        MessageHolder.messages.add(MessageConst.TEST_METHOD_START);

        EnumSingleton enumSingleton = EnumSingleton.INSTANCE;
        enumSingleton.doWork();

        MessageHolder.messages.add(MessageConst.TEST_METHOD_FINISH);

        assertEquals(expectedMessageSequence, MessageHolder.messages);
    }
}
