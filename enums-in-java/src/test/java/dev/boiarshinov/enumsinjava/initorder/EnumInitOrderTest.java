package dev.boiarshinov.enumsinjava.initorder;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumInitOrderTest {

    static {
        MessageHolder.messages.add("Test static block");
    }

    /**
     * Все кодовые блоки в перечислении выполняются только при первом вызове инстанса.
     * Так происходит, потому что по спецификации Java статические кодовые блоки
     * выполняются не при загрузке класса, а при первом обращении к его статическому полю.
     * <p>
     * Статический блок в перечислении выполнился позже, потому что обычные кодовые блоки
     * в перечислении преобразуются в статические, а экземпляры перечисления преобразуются
     * в статические поля.
     * При этом порядок получается следующим:
     * <li>статический кодовый блок, полученный из кодового блока</li>
     * <li>статическое поле</li>
     * <li>статический кодовый блок, полученный из статического кодового блока</li>
     * <p>
     * Так как все статические члены выполняются последовательно, то порядок получается
     * такой, как указано в тесте
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-12.html#jls-12.4.1">спека</a>
     */
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

        EnumInitOrder enumInitOrder = EnumInitOrder.INSTANCE;
        enumInitOrder.doWork();

        MessageHolder.messages.add(MessageConst.TEST_METHOD_FINISH);

        assertEquals(expectedMessageSequence, MessageHolder.messages);
    }
}
