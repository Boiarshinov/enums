package dev.boiarshinov.enumsinapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JacksonEnumSerializeTest {

    @Test
    void serializeEnum() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals("\"MONDAY\"", objectMapper.writeValueAsString(DayOfWeek.MONDAY));
    }
}
