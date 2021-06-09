package dev.boiarshinov.enumsinapi.api;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class TestResult {

    private long testId;
    private long teacherId;
    private Grade grade;

    @Getter
    @RequiredArgsConstructor
    public enum Grade {
        EXCELLENT('A'),
        SATISFACTORY('B'),
        MEDIOCRE('C'),
        INSUFFICIENT('D'),
        FAILURE('F');

        @JsonValue
        private final char mark;
    }
}
