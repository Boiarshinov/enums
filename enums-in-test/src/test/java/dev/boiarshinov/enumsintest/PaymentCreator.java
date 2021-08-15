package dev.boiarshinov.enumsintest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentCreator {

    private static final String PERSONAL_INN = "012345678901";
    private static final String JURIDICAL_INN = "0123456789";

    public static Payment create(
        PeriodType periodType,
        PayerType payerType
    ) {
        Period period;
        switch (periodType) {
            case ONE_TIME: {
                period = new Period.OneTime(LocalDate.parse("2021-03-08"));
                break;
            }
            case PERIODIC: {
                period = new Period.Periodic("JAN", "month");
                break;
            }
            case FROM_TO:
            default: {
                period = new Period.FromTo(
                    LocalDateTime.parse("2021-03-08T15:30:00"),
                    LocalDateTime.parse("2021-03-08T16:25:15")
                );
            }
        }

        String inn;
        Payer payer;
        switch (payerType) {
            case PERSON: {
                inn = PERSONAL_INN;
                payer = new Payer.Person("Санёк", "+78005553535");
                break;
            }
            case INDIVIDUAL_MERCHANT: {
                inn = PERSONAL_INN;
                payer = new Payer.IndividualMerchant("ИП Поциков А.В.", "321011234567890");
                break;
            }
            case JURIDICAL:
            default: {
                inn = JURIDICAL_INN;
                payer = new Payer.Organisation("ООО Рога и Копыта", "123456789");
            }
        }

        return Payment.builder()
            .amount(1000)
            .currency("RUR")
            .purpose("Аренда пенька")
            .inn(inn)
            .period(period)
            .payer(payer)
            .build();
    }


    public enum PeriodType {
        ONE_TIME,
        PERIODIC,
        FROM_TO
    }

    public enum PayerType {
        PERSON,
        INDIVIDUAL_MERCHANT,
        JURIDICAL
    }
}
