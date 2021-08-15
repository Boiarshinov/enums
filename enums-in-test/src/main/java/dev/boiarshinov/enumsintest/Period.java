package dev.boiarshinov.enumsintest;

import lombok.Value;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface Period {

    @Value
    class OneTime implements Period {
        LocalDate date;
    }

    @Value
    class Periodic implements Period {
        String value;
        String periodType;
    }

    @Value
    class FromTo implements Period {
        LocalDateTime from;
        LocalDateTime to;
    }
}
