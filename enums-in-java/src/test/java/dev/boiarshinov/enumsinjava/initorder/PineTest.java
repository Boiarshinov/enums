package dev.boiarshinov.enumsinjava.initorder;

import org.junit.jupiter.api.Test;

//Not real tests. Just triggers to look at the console output
class PineTest {

    @Test
    void initializeEnum() {
        System.out.println("Before enum first call");
        var fir = Pine.FIR;
    }

    @Test
    void initializeNotEnum() {
        System.out.println("Before enum first call");
        var fir = PineIsNotEnum.FIR;
    }

}