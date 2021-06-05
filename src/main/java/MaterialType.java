import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MaterialType {
    ATOMIC_OBJECT("task"),
    TEST_SPECIFICATION("spec");

    private final String restName;
}
