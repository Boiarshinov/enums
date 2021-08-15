package dev.boiarshinov.enumsintest;

import lombok.Value;

public interface Payer {

    @Value
    class Person implements Payer {
        String name;
        String phone;
    }

    @Value
    class IndividualMerchant implements Payer {
        String ip_name;
        String ogrnip;
    }

    @Value
    class Organisation implements Payer {
        String title;
        String kpp;
    }
}
