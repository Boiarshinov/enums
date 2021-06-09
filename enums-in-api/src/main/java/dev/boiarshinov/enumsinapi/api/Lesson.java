package dev.boiarshinov.enumsinapi.api;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;

@Data
@Builder
public class Lesson {

    private String discipline;
    private DayOfWeek dayOfWeek;
    private int order;
    private String cabinet;
}
