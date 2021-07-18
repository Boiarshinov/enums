package dev.boiarshinov.enumsinapi.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MISSING_INPUT_SECRET("missing-input-secret"),
    INVALID_INPUT_SECRET("invalid-input-secret"),
    MISSING_INPUT_RESPONSE("missing-input-response"),
    INVALID_INPUT_RESPONSE("invalid-input-response"),
    BAD_REQUEST("bad-request"),
    TIMEOUT_OR_DUPLICATE("timeout-or-duplicate"),
    INVALID_KEYS("invalid-keys"), //Не описанный в документации, но существующий код ошибки
    UNEXPECTED("unexpected");

    private final String value;

    public static final EnumSet<ErrorCode> RECOVERABLE_ERRORS = EnumSet.of(
        MISSING_INPUT_RESPONSE,
        INVALID_INPUT_RESPONSE,
        TIMEOUT_OR_DUPLICATE
    );

    private static final Map<String, ErrorCode> VALUE_TO_ENUM = EnumSet.allOf(ErrorCode.class).stream()
        .collect(Collectors.toMap(ErrorCode::getValue, Function.identity()));

    //good way
    @JsonCreator
    public static ErrorCode parse(String value) {
        return Optional.ofNullable(VALUE_TO_ENUM.get(value))
                .orElse(ErrorCode.UNEXPECTED);
    }

    public static ErrorCode valueOfOrDefault(String name) {
        try {
            return ErrorCode.valueOf(name);
        } catch (IllegalArgumentException ignored) {
            return ErrorCode.UNEXPECTED;
        }
    }

    //bad way
    public static ErrorCode from(String value) {
        return Arrays.stream(ErrorCode.values())
            .filter(it -> it.getValue().equals(value))
            .findFirst()
            .orElse(ErrorCode.UNEXPECTED);
    }

    public static Set<String> getCodes() {
        return VALUE_TO_ENUM.keySet();
    }
}
