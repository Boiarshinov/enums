package dev.boiarshinov.enumsinapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.boiarshinov.enumsinapi.api.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.DayOfWeek;

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

    @Test
    void deserializeViaJsonCreator() throws JsonProcessingException {
        ErrorCode errorCode = objectMapper.readValue("\"unexpected-value\"", ErrorCode.class);
        assertEquals(ErrorCode.UNEXPECTED, errorCode);
    }
}
