package dev.boiarshinov.enumsinapi.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.DayOfWeek;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JacksonEnumSerializeTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void serializeEnum() throws JsonProcessingException {
        String day = objectMapper.writeValueAsString(DayOfWeek.MONDAY);
        assertEquals("\"MONDAY\"", day);
    }

    @Test
    void deserializeEnum() throws JsonProcessingException {
        DayOfWeek day = objectMapper.readValue("\"MONDAY\"", DayOfWeek.class);
        assertEquals(DayOfWeek.MONDAY, day);
    }

    @RequiredArgsConstructor
    private enum ResponseCode {
        SUCCESS("I0000"),
        VALIDATION_FAIL("I2000"),
        INTERNAL_ERROR("I5000"),
        UNKNOWN_ERROR("I9999");

        @JsonValue
        private final String code;

        @JsonCreator
        static ResponseCode parse(String code) {
            return Arrays.stream(values())
                .filter(it -> it.code.equals(code))
                .findFirst()
                .orElse(UNKNOWN_ERROR);
        }
    }

    @Test
    void deserializeViaJsonCreator() throws JsonProcessingException {
        var errorCode = objectMapper.readValue("\"I6666\"", ResponseCode.class);
        assertEquals(ResponseCode.UNKNOWN_ERROR, errorCode);
    }

    @Test
    void serializeViaJsonValue() throws JsonProcessingException {
        String code = objectMapper.writeValueAsString(ResponseCode.VALIDATION_FAIL);
        assertEquals("\"I2000\"", code);
    }
}
