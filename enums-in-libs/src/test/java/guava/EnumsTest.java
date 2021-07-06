package guava;

import com.google.common.base.Converter;
import com.google.common.base.Enums;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumsTest {

    @Test
    void getField() {
        Field field = Enums.getField(HttpCode.OK_200);

        assertEquals(0, field.getDeclaredAnnotations().length);
    }

    @Test
    void stringConverter() {
        Converter<String, HttpCode> converter = Enums.stringConverter(HttpCode.class);
        HttpCode result = converter.convert("REDIRECT_300");

        assertEquals(HttpCode.REDIRECT_300, result);
    }

    @Test
    void getIfPresent() {
        HttpCode result = Enums.getIfPresent(HttpCode.class, "BAD_REQUEST_400").get();

        assertEquals(HttpCode.BAD_REQUEST_400, result);
    }

    private enum HttpCode {
        OK_200, REDIRECT_300, BAD_REQUEST_400, SERVER_ERROR_500
    }
}
