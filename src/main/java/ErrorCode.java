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
    INVALID_KEYS("invalid-keys"); //Не описанный в документации, но существующий код ошибки

    private final String value;

    private static final Map<String, ErrorCode> VALUE_TO_ENUM = EnumSet.allOf(ErrorCode.class).stream()
        .collect(Collectors.toMap(ErrorCode::getValue, Function.identity()));

    //good way
    public static ErrorCode byCount(String value) {
        return Optional.ofNullable(VALUE_TO_ENUM.get(value))
            .orElseThrow(() -> new IllegalArgumentException(String.format("Have no code for value '%s'", value)));
    }

    //bad way
    public static ErrorCode fromCount(String value) {
        return Arrays.stream(ErrorCode.values())
            .filter(it -> it.getValue().equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("Have no code for value '%s'", value)));
    }

    public static Set<String> getCodes() {
        return VALUE_TO_ENUM.keySet();
    }
}
