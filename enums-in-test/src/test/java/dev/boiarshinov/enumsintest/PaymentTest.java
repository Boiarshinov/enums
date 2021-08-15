package dev.boiarshinov.enumsintest;

import org.junit.jupiter.api.Test;

public class PaymentTest {

    @Test
    void test() {
        var payment = PaymentCreator.create(
            PaymentCreator.PeriodType.ONE_TIME,
            PaymentCreator.PayerType.JURIDICAL
        );
        // act, assert
    }
}
