import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MaterialType {
    ATOMIC_OBJECT("task"),
    TEST_SPECIFICATION("spec");

    private final String restName;
}
